package controllers.mercure

import java.util.UUID

import com.typesafe.config.ConfigFactory
import model.mercure.daos.category.Category
import services.mercure.category.CategoryService
import services.mercure.user.UserService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles
import com.typesafe.config.ConfigFactory
import fly.play.s3.{ BucketFile, S3, S3Exception }
import org.apache.commons.io.FileUtils
import net.ceedubs.ficus.Ficus._

import scala.concurrent.Future

/**
 * Created by madalien on 03/03/15.
 */

import javax.inject.Inject

import com.mohiva.play.silhouette.api.Silhouette
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.api.DB
import reactivemongo.bson.BSONDocument
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.ExecutionContext.Implicits.global
// Reactive Mongo imports

// BSON-JSON conversions/collection

class CategoryCtrl @Inject() (
    val messagesApi: MessagesApi,
    val userService: UserService,
    val categoryService: CategoryService,
    implicit val application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {

  val logger = Logger("MR." + this.getClass.getSimpleName);

  def create = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[Category]) {
    request =>
      {
        val uid = UUID.randomUUID()
        val category = request.body.copy(id = Some(uid))
        categoryService.insert(category).map {
          res =>
            res.inError match {
              case true => {
                InternalServerError(Json.obj("res" -> s" ${res.message}  code: ${res.code}"))
              }
              case false => {
                Created(Json.obj("res" -> Json.obj("id" -> uid.toString)))
              }
            }
        }

      }
  }

  def update(id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[Category]) {
    request =>
      {

        val category = request.body
        val selec = BSONDocument("id" -> id);

        categoryService.findAndModify(selec, category, BSONDocument()) map {
          mayOldCat =>
            {
              mayOldCat match {
                case Some(oldCat: Category) => {
                  Ok(Json.obj("res" -> "Atelier successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating Atelier"))
              }
            }
        }

      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    request =>
      {
        //logger.debug(s"cat ids =>[$ids]")
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        categoryService.remove(query).map {
          res =>
            res.inError match {
              case true => {

                InternalServerError(Json.obj("res" -> s" ${res.message}  code: ${res.code}"))
              }
              case false => {
                Ok(Json.obj("res" -> s" ${res.message} Remove Done"))
              }
            }
        }
      }
  }

  def categoryList(page: Int, numberByPage: Int, first_created_date: Option[Boolean]) = Action.async {
    request =>
      {
        var sort = Json.obj("updated_date" -> -1)

        if (first_created_date.isDefined) {
          sort = Json.obj("updated_date" -> (if (first_created_date.get) -1 else 1))
        }

        categoryService.getList(page, numberByPage, sort) map {
          subArr => Ok(Json.toJson(subArr))
        }
      }
  }

  def count() = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        categoryService.count(Json.obj()).map {
          nbUsr => Ok(Json.obj("res" -> nbUsr))
        }
      }
  }

  def uploadLogo(category_id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.multipartFormData) {
    request =>
      request.body.file("file").map { file =>
        import java.io.File
        val currentPath = application.path.getAbsolutePath
        val filename = file.filename.replace(' ', '_')
        val filePath = currentPath + "/category/" + category_id + "/" + filename;
        val fileUrl = "logos/category/" + category_id + "/" + filename;
        val contentType = file.contentType
        //val title = request.body.asFormUrlEncoded.get("title").get(0);
        val dir = new File(currentPath + "/category/" + category_id)
        dir.mkdirs()
        logger.debug(s"Application cur path [$currentPath]")
        val fileFromDisk = file.ref.moveTo(new File(filePath))

        val bucketName = ConfigFactory.load().as[String]("s3.bucketName")
        val bucket = S3(bucketName)
        val byteArr = FileUtils.readFileToByteArray(fileFromDisk)
        val result = bucket add BucketFile(fileUrl, file.contentType.getOrElse("text/plain"), byteArr, None, None)
        result.flatMap {
          unit =>
            {
              val awsFileUrl = bucket.s3.url(bucketName, fileUrl)
              logger.debug(s"Aws result $unit")

              val subModifier = BSONDocument(
                "$set" -> BSONDocument(
                  "avatar_path" -> awsFileUrl
                )
              )

              val selec = BSONDocument("id" -> category_id);
              categoryService.update(selec, subModifier) map {
                res =>
                  res.inError match {
                    case true => {
                      InternalServerError(Json.obj("res" -> s" ${res.message}  code: ${res.code}"))
                    }
                    case false => {
                      Ok(Json.obj("res" -> Json.obj("avatar_path" -> awsFileUrl)))
                    }
                  }
              }

            }
        }.recover {
          case S3Exception(status, code, message, originalXml) =>
            {
              logger.error("Error: " + message)
              BadRequest(Json.obj("result" -> "main logo error: missing file or existing file"))
            }
        }

      }.getOrElse {
        Future.successful(BadRequest(Json.obj("result" -> "atelier error: missing file or existing file")))
      }
  }

}
