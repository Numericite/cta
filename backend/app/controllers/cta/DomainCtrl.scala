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
import models.daos.domain.Domain
import org.apache.commons.io.FileUtils
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.domain.DomainService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

import scala.concurrent.Future

class DomainCtrl @Inject() (
    val messagesApi: MessagesApi,
    val domainService: DomainService,
    implicit val application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);

  import models.daos.domain.DomainFormats._

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Domain]) {
    implicit request =>
      {
        val domain = request.body
        val uid = UUID.randomUUID()

        val docWithId = domain.copy(id = Some(uid), updated_date = Some(new DateTime()))
        domainService.insert(docWithId).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Domain]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        domainService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldDomain =>
            {
              mayOldDomain match {
                case Some(oldDomain: Domain) => {
                  Ok(Json.obj("res" -> "Domain successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating domain"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        domainService.remove(query).map {
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
    kind: Option[String]
  ) = Action.async {
    implicit request =>
      {
        var query = BSONDocument()

        if (kind.nonEmpty) {
          query ++= BSONDocument("kind" -> kind)
        }

        domainService.find(query, page = page, numberPerPage = numberPerPage) map {
          domainArr => Ok(Json.toJson(domainArr))
        }
      }
  }

  def count(kind: Option[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        var query = Json.obj()

        if (kind.nonEmpty) {
          query ++= Json.obj("kind" -> kind)
        }

        domainService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        domainService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          domainArr => Ok(Json.toJson(domainArr))
        }
      }
  }

  def uploadLogo(domain_id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.multipartFormData) {
    request =>
      request.body.file("file").map { file =>
        import java.io.File
        val currentPath = application.path.getAbsolutePath
        val filename = file.filename.replace(' ', '_')
        val filePath = currentPath + "/domain/" + domain_id + "/" + filename;
        val fileUrl = "logos/domain/" + domain_id + "/" + filename;
        val contentType = file.contentType
        //val title = request.body.asFormUrlEncoded.get("title").get(0);
        val dir = new File(currentPath + "/domain/" + domain_id)
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

              val updatedDomain = BSONDocument(
                "$set" -> BSONDocument(
                  "img_path" -> awsFileUrl
                )
              )

              val selec = BSONDocument("id" -> domain_id);
              domainService.update(selec, updatedDomain) map {
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
