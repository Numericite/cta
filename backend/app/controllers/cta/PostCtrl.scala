package controllers.cta

/**
 * Created by Clément Lelong on 27/05/16.
 */

import java.util.UUID

import models.daos.post.Post
import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.typesafe.config.ConfigFactory
import fly.play.s3.{ BucketFile, S3, S3Exception }
import net.ceedubs.ficus.Ficus._
import org.apache.commons.io.FileUtils
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles
import services.post.PostService

import scala.concurrent.Future

class PostCtrl @Inject() (
    val messagesApi: MessagesApi,
    val postService: PostService,
    implicit val application: play.api.Application,
    passwordHasher: PasswordHasher,
    authInfoRepository: AuthInfoRepository,

    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("BP." + this.getClass.getSimpleName);

  import models.daos.post.PostFormats._

  def create = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[Post]) {
    request =>
      {
        val uid = UUID.randomUUID()
        val post = request.body.copy(id = Some(uid), isPinned = Some(false), updated_date = Some(new DateTime()), created_date = Some(new DateTime()))
        postService.insert(post).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[Post]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        logger.debug(s"$elemToUpdate")
        postService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldUsr =>
            {
              mayOldUsr match {
                case Some(oldUsr: Post) => {
                  Ok(Json.obj("res" -> Json.toJson(oldUsr)))
                }
                case _ => InternalServerError(Json.obj("res" -> s"error while updating post"))
              }
            }
        }
      }
  }

  def updateStatus(post_id: String, renewal: Boolean) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json) {

    implicit request =>
      {
        (request.body \ "status_name").validate[String].fold(
          error => { Future.successful(BadRequest(s"error")) },
          status_name => {

            var fieldsToUpdate = BSONDocument(
              "status_name" -> status_name
            )

            if (renewal) {
              fieldsToUpdate ++= BSONDocument(
                "updated_date" -> DateTime.now.getMillis
              )
            }
            val subModifier = BSONDocument(
              "$set" -> fieldsToUpdate
            )

            val selec = BSONDocument("id" -> post_id);
            postService.update(selec, subModifier) map {
              res =>
                res.inError match {
                  case true => {
                    InternalServerError(Json.obj("res" -> s" ${res.message}  code: ${res.code}"))
                  }
                  case false => {
                    Ok(Json.obj("res" -> s" ${res.message} updated Done"))
                  }
                }
            }
          }
        )

      }
  }

  def getList(page: Int, numberPerPage: Int, status_names: Option[List[String]], title: Option[String], exclude_ids: Option[List[String]], pinFirst: Option[Boolean]) = Action.async {
    implicit request =>
      {
        var query = BSONDocument()

        if (status_names.get.nonEmpty) {
          query ++= BSONDocument("status_name" -> BSONDocument("$in" -> status_names))
        }

        if (title.nonEmpty) {
          query ++= BSONDocument("title" -> BSONDocument("$regex" -> title.get, "$options" -> "i"))
        }

        if (exclude_ids.get.nonEmpty) {
          query ++= BSONDocument("id" -> BSONDocument("$nin" -> exclude_ids))
        }

        var sort = Json.obj("date" -> -1)
        if (pinFirst.getOrElse(false)) {
          sort = Json.obj("isPinned" -> -1, "date" -> -1)
        }

        postService.find(query, page = page, numberPerPage = numberPerPage, sort = sort) map {
          subArr => Ok(Json.toJson(subArr))
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        postService.remove(query).map {
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

  def getByIds(ids: List[String]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        postService.find(query).map {
          subArr => Ok(Json.toJson(subArr))
        }
      }
  }

  def uploadLogo(post_id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.multipartFormData) {
    request =>
      request.body.file("file").map { file =>
        import java.io.File
        val currentPath = application.path.getAbsolutePath
        val filename = file.filename.replace(' ', '_')
        val filePath = currentPath + "/post/" + post_id + "/" + filename;
        val fileUrl = "logos/post/" + post_id + "/" + filename;
        val contentType = file.contentType
        //val title = request.body.asFormUrlEncoded.get("title").get(0);
        val dir = new File(currentPath + "/post/" + post_id)
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

              val updatedPost = BSONDocument(
                "$set" -> BSONDocument(
                  "avatar_path" -> awsFileUrl
                )
              )

              val selec = BSONDocument("id" -> post_id);
              postService.update(selec, updatedPost) map {
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

  def count(status_names: Option[List[String]], title: Option[String]) = Action.async {
    implicit request =>
      {
        var query = Json.obj()

        if (status_names.get.nonEmpty) {
          query ++= Json.obj("status_name" -> Json.obj("$in" -> status_names))
        }

        if (title.nonEmpty) {
          query ++= Json.obj("title" -> Json.obj("$regex" -> title.get, "$options" -> "i"))
        }

        postService.count(query).map {
          nbPropos => Ok(Json.obj("res" -> nbPropos))
        }
      }
  }

}
