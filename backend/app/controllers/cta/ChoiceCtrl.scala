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
import models.daos.choice.Choice
import org.apache.commons.io.FileUtils
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.choice.ChoiceService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

import scala.concurrent.Future

class ChoiceCtrl @Inject() (
    val messagesApi: MessagesApi,
    val choiceService: ChoiceService,
    implicit val application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);

  import models.daos.choice.ChoiceFormats._

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Choice]) {
    implicit request =>
      {
        val choice = request.body
        val uid = UUID.randomUUID()

        val docWithId = choice.copy(id = Some(uid), updated_date = Some(choice.updated_date.getOrElse(new DateTime())))
        choiceService.insert(docWithId).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Choice]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val choice = BSONDocument("id" -> id);
        choiceService.findAndModify(choice, elemToUpdate, BSONDocument()) map {
          mayOldChoice =>
            {
              mayOldChoice match {
                case Some(oldChoice: Choice) => {
                  Ok(Json.obj("res" -> "Choice successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating choice"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        choiceService.remove(query).map {
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
    selection_ids: Option[List[String]]
  ) = Action.async {
    implicit request =>
      {
        var query = BSONDocument()

        if (selection_ids.get.nonEmpty) {
          query ++= BSONDocument("selection_id" -> BSONDocument("$in" -> selection_ids))
        }

        choiceService.find(query, page = page, numberPerPage = numberPerPage) map {
          choiceArr => Ok(Json.toJson(choiceArr))
        }
      }
  }

  def count(selection_ids: Option[List[String]]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        var query = Json.obj()

        if (selection_ids.get.nonEmpty) {
          query ++= Json.obj("selection_id" -> Json.obj("$in" -> selection_ids))
        }

        choiceService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        choiceService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          choiceArr => Ok(Json.toJson(choiceArr))
        }
      }
  }

  def uploadLogo(choice_id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.multipartFormData) {
    request =>
      request.body.file("file").map { file =>
        import java.io.File
        val currentPath = application.path.getAbsolutePath
        val filename = file.filename.replace(' ', '_')
        val filePath = currentPath + "/choice/" + choice_id + "/" + filename;
        val fileUrl = "logos/choice/" + choice_id + "/" + filename;
        val contentType = file.contentType
        //val title = request.body.asFormUrlEncoded.get("title").get(0);
        val dir = new File(currentPath + "/choice/" + choice_id)
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

              val updatedChoice = BSONDocument(
                "$set" -> BSONDocument(
                  "img_path" -> awsFileUrl
                )
              )

              val selec = BSONDocument("id" -> choice_id);
              choiceService.update(selec, updatedChoice) map {
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
