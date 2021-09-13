package controllers.cta

/**
 * Created by Clément Lelong O8/05/17
 */
import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.typesafe.config.ConfigFactory
import fly.play.s3.{ BucketFile, S3, S3Exception }
import net.ceedubs.ficus.Ficus._
import models.daos.selection.Selection
import org.apache.commons.io.FileUtils
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.selection.SelectionService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

import scala.concurrent.Future

class SelectionCtrl @Inject() (
    val messagesApi: MessagesApi,
    val selectionService: SelectionService,
    implicit val application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);

  import models.daos.selection.SelectionFormats._

  def create = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[Selection]) {
    implicit request =>
      {
        val selection = request.body
        val uid = UUID.randomUUID()

        val docWithId = selection.copy(id = Some(uid), updated_date = Some(selection.updated_date.getOrElse(new DateTime())))
        selectionService.insert(docWithId).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[Selection]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selection = BSONDocument("id" -> id);
        selectionService.findAndModify(selection, elemToUpdate, BSONDocument()) map {
          mayOldSelection =>
            {
              mayOldSelection match {
                case Some(oldSelection: Selection) => {
                  Ok(Json.obj("res" -> "Selection successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating selection"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        selectionService.remove(query).map {
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
    activity_ids: Option[List[String]],
    version_ids: Option[List[String]]
  ) = Action.async {
    implicit request =>
      {
        var query = BSONDocument()

        if (activity_ids.get.nonEmpty) {
          query ++= BSONDocument("activity_id" -> BSONDocument("$in" -> activity_ids))
        }

        if (version_ids.get.nonEmpty) {
          query ++= BSONDocument("version_id" -> BSONDocument("$in" -> version_ids))
        }

        selectionService.find(query, page = page, numberPerPage = numberPerPage) map {
          selectionArr => Ok(Json.toJson(selectionArr))
        }
      }
  }

  def count(activity_ids: Option[List[String]], version_ids: Option[List[String]]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        var query = Json.obj()

        if (activity_ids.get.nonEmpty) {
          query ++= Json.obj("activity_id" -> Json.obj("$in" -> activity_ids))
        }

        if (version_ids.get.nonEmpty) {
          query ++= Json.obj("version_id" -> Json.obj("$in" -> version_ids))
        }

        selectionService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        selectionService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          selectionArr => Ok(Json.toJson(selectionArr))
        }
      }
  }

  def uploadLogo(selection_id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.multipartFormData) {
    request =>
      request.body.file("file").map { file =>
        import java.io.File
        val currentPath = application.path.getAbsolutePath
        val filename = file.filename.replace(' ', '_')
        val filePath = currentPath + "/selection/" + selection_id + "/" + filename;
        val fileUrl = "logos/selection/" + selection_id + "/" + filename;
        val contentType = file.contentType
        //val title = request.body.asFormUrlEncoded.get("title").get(0);
        val dir = new File(currentPath + "/selection/" + selection_id)
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

              val updatedSelection = BSONDocument(
                "$set" -> BSONDocument(
                  "img_path" -> awsFileUrl
                )
              )

              val selec = BSONDocument("id" -> selection_id);
              selectionService.update(selec, updatedSelection) map {
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
