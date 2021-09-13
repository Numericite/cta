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
import models.daos.school.School
import org.apache.commons.io.FileUtils
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.school.SchoolService
import services.school.classroom.SchoolClassroomService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

import scala.concurrent.Future

class SchoolCtrl @Inject() (
    val messagesApi: MessagesApi,
    val schoolService: SchoolService,
    val schoolClassroomService: SchoolClassroomService,
    implicit val application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);

  import models.daos.school.SchoolFormats._

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[School]) {
    implicit request =>
      {
        val school = request.body
        val uid = UUID.randomUUID()

        val docWithId = school.copy(id = Some(uid), updated_date = Some(new DateTime()))
        schoolService.insert(docWithId).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[School]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        schoolService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldSchool =>
            {
              mayOldSchool match {
                case Some(oldSchool: School) => {
                  Ok(Json.obj("res" -> "School successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating school"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        schoolService.remove(query).map {
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
    kinds: Option[List[String]]
  ) = Action.async {
    implicit request =>
      {
        var query = BSONDocument()

        if (kinds.get.nonEmpty) {
          query ++= BSONDocument("kind" -> BSONDocument("$in" -> kinds))
        }

        schoolService.find(query, page = page, numberPerPage = numberPerPage) map {
          schoolArr => Ok(Json.toJson(schoolArr))
        }
      }
  }

  def getGradesBySchools(school_ids: Option[List[String]]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("school_id" -> BSONDocument("$in" -> school_ids))

        for {
          classrooms <- schoolClassroomService.find(query)
        } yield {
          Ok(Json.toJson(classrooms.map(_.grade).distinct))
        }

      }
  }

  def count(kinds: Option[List[String]]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        var query = Json.obj()

        if (kinds.get.nonEmpty) {
          query ++= Json.obj("kind" -> Json.obj("$in" -> kinds))
        }

        schoolService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        schoolService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          schoolArr => Ok(Json.toJson(schoolArr))
        }
      }
  }

  def uploadLogo(school_id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.multipartFormData) {
    request =>
      request.body.file("file").map { file =>
        import java.io.File
        val currentPath = application.path.getAbsolutePath
        val filename = file.filename.replace(' ', '_')
        val filePath = currentPath + "/school/" + school_id + "/" + filename;
        val fileUrl = "logos/school/" + school_id + "/" + filename;
        val contentType = file.contentType
        //val title = request.body.asFormUrlEncoded.get("title").get(0);
        val dir = new File(currentPath + "/school/" + school_id)
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

              val updatedSchool = BSONDocument(
                "$set" -> BSONDocument(
                  "img_path" -> awsFileUrl
                )
              )

              val selec = BSONDocument("id" -> school_id);
              schoolService.update(selec, updatedSchool) map {
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
