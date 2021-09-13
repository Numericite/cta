package controllers.cta

import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.typesafe.config.ConfigFactory
import fly.play.s3.{ BucketFile, S3, S3Exception }
import models.daos.faq.Faq
import models.daos.module.activity.ModuleActivity
import net.ceedubs.ficus.Ficus.stringValueReader
import net.ceedubs.ficus.FicusConfig.toFicusConfig
import org.apache.commons.io.FileUtils
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.mercure.user.UserService
import services.module.activity.ModuleActivityService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

import scala.concurrent.Future

/**
 * Created by Clement LELONG on 13/06/19.
 */

class ModuleActivityCtrl @Inject() (
    val messagesApi: MessagesApi,
    val userService: UserService,
    val moduleActivityService: ModuleActivityService,
    implicit val application: play.api.Application,
    passwordHasher: PasswordHasher,
    authInfoRepository: AuthInfoRepository,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("MR." + this.getClass.getSimpleName);

  import models.daos.module.activity.ModuleActivityFormats._

  def create = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[ModuleActivity]) {
    implicit request =>
      {
        val moduleActivity = request.body
        val uid = UUID.randomUUID()

        val docWithId = moduleActivity.copy(id = Some(uid), created_date = Some(new DateTime()), updated_date = Some(new DateTime()))
        moduleActivityService.insert(docWithId).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[ModuleActivity]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        moduleActivityService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldModuleActivity =>
            {
              mayOldModuleActivity match {
                case Some(oldModuleActivity: ModuleActivity) => {
                  Ok(Json.obj("res" -> "Module successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating module"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        moduleActivityService.remove(query).map {
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

  def getList(
    page: Int,
    numberPerPage: Int,
    module_ids: Option[List[String]]
  ) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        var query = BSONDocument()

        if (module_ids.get.nonEmpty) {
          query ++= BSONDocument("module_id" -> BSONDocument("$in" -> module_ids))
        }

        moduleActivityService.find(query, page = page, numberPerPage = numberPerPage) map {
          moduleArr => Ok(Json.toJson(moduleArr))
        }
      }
  }

  def count(module_ids: Option[List[String]]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        var query = Json.obj()

        if (module_ids.get.nonEmpty) {
          query ++= Json.obj("module_id" -> Json.obj("$in" -> module_ids))
        }

        moduleActivityService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        moduleActivityService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          moduleArr => Ok(Json.toJson(moduleArr))
        }
      }
  }

  def uploadDocument(module_activity_id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.multipartFormData) {
    request =>
      request.body.file("file").map { file =>
        import java.io.File
        val currentPath = application.path.getAbsolutePath
        val filename = file.filename.replace(' ', '_')
        val filePath = currentPath + "/moduleActivity/" + module_activity_id + "/" + filename;
        val fileUrl = "documents/moduleActivity/" + module_activity_id + "/" + filename;
        val contentType = file.contentType
        //val title = request.body.asFormUrlEncoded.get("title").get(0);
        val dir = new File(currentPath + "/moduleActivity/" + module_activity_id)
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

              val updatedModuleActivity = BSONDocument(
                "$set" -> BSONDocument(
                  "path" -> awsFileUrl
                )
              )

              val selec = BSONDocument("id" -> module_activity_id);
              moduleActivityService.update(selec, updatedModuleActivity) map {
                res =>
                  res.inError match {
                    case true => {
                      InternalServerError(Json.obj("res" -> s" ${res.message}  code: ${res.code}"))
                    }
                    case false => {
                      Ok(Json.obj("res" -> Json.obj("path" -> awsFileUrl)))
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
