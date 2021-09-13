package controllers.cta

import java.io.File
import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.typesafe.config.ConfigFactory
import models.daos.document.Document
import org.apache.commons.io.FileUtils
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.document.DocumentService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles
import play.api.libs.concurrent.Execution.Implicits._
import fly.play.s3.{ BucketFile, S3, S3Exception }
import org.apache.commons.io.FileUtils
import net.ceedubs.ficus.Ficus._
import org.joda.time.DateTime

import scala.concurrent.Future

/**
 * Created by madalien on 25/05/16.
 */

class DocumentCtrl @Inject() (
    val messagesApi: MessagesApi,
    val documentService: DocumentService,
    implicit val application: play.api.Application,
    passwordHasher: PasswordHasher,
    authInfoRepository: AuthInfoRepository,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Document]) {
    implicit request =>
      {
        val document = request.body
        val uid = UUID.randomUUID()

        val docWithId = document.copy(id = Some(uid), updated_date = Some(new DateTime()))
        documentService.insert(docWithId).map {
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

  def getList(parent_type: Option[String], parent_types: Option[List[String]], parent_ids: Option[List[String]], grades: Option[List[Int]], school_kinds: Option[List[String]], page: Int, numberPerPage: Int) = Action.async {
    implicit request =>
      {
        var query = BSONDocument()

        if (school_kinds.get.nonEmpty) {
          query ++= BSONDocument("school_kind" -> BSONDocument("$in" -> school_kinds))
        }

        if (parent_type.isDefined) {
          query ++= BSONDocument("parent_type" -> parent_type)
        }

        if (school_kinds.get.nonEmpty) {
          query ++= BSONDocument("school_kind" -> BSONDocument("$in" -> school_kinds))
        }

        if (parent_types.get.nonEmpty) {
          query ++= BSONDocument("parent_type" -> BSONDocument("$in" -> parent_types))
        }

        if (parent_ids.get.nonEmpty) {
          query ++= BSONDocument("parent_id" -> BSONDocument("$in" -> parent_ids))
        }

        if (grades.get.nonEmpty) {
          query ++= BSONDocument("grades" -> BSONDocument("$in" -> grades))
        }

        documentService.find(query, Json.obj("updated_date" -> -1), page, numberPerPage) map {
          docArr => Ok(Json.toJson(docArr))
        }
      }
  }

  def count(parent_type: Option[String], parent_types: Option[List[String]], parent_ids: Option[List[String]], grades: Option[List[Int]], school_kinds: Option[List[String]]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        var query = Json.obj()

        if (parent_type.isDefined) {
          query ++= Json.obj("parent_type" -> parent_type)
        }

        if (school_kinds.get.nonEmpty) {
          query ++= Json.obj("school_kind" -> Json.obj("$in" -> school_kinds))
        }

        if (parent_types.get.nonEmpty) {
          query ++= Json.obj("parent_type" -> Json.obj("$in" -> parent_types))
        }

        if (parent_ids.get.nonEmpty) {
          query ++= Json.obj("parent_id" -> Json.obj("$in" -> parent_ids))
        }

        if (grades.get.nonEmpty) {
          query ++= Json.obj("grades" -> Json.obj("$in" -> grades))
        }

        documentService.count(query) map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def update(id: String) = silhouette.SecuredAction(WithRoles("NormalUser", "Partner")).async(parse.json[Document]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        documentService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldDocument =>
            {
              mayOldDocument match {
                case Some(oldDocument: Document) => {
                  Ok(Json.obj("res" -> "Document successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating document"))
              }
            }
        }
      }
  }

  //to be test
  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {

        import java.io.File
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        documentService.find(query, page = 1, numberPerPage = 1000) flatMap {
          docList =>
            {
              docList.foreach {
                doc =>
                  val realpath = doc.path.substring(doc.path.indexOf("/") + 1)
                  var deleteFile: File = new File(realpath)
                  deleteFile.delete()
              }

              documentService.remove(query).map {
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
      }
  }

  def upload(parent_type: String) = Action.async(parse.multipartFormData) {
    implicit request =>
      request.body.file("file").map {
        file =>
          {
            val currentPath = application.path.getPath
            val filename = file.filename.replace(' ', '_')
            val filePath = currentPath + "/documents/" + parent_type + "/" + filename;
            val fileUrl = "download/documents/" + parent_type + "/" + filename;
            val contentType = file.contentType
            val dir = new File(currentPath + "/documents/" + parent_type)
            dir.mkdirs()
            val fileFromDisk = file.ref.moveTo(new File(filePath))

            val only_upload = request.body.asFormUrlEncoded.get("only_upload").get(0).toBoolean
            val desc = request.body.asFormUrlEncoded.get("description").get(0);
            val user_id = request.body.asFormUrlEncoded.get("user_id").get(0);
            val title = request.body.asFormUrlEncoded.get("title").get(0);
            val school_kind = request.body.asFormUrlEncoded.get("school_kind").getOrElse(Seq(""))(0)
            val file_kind = request.body.asFormUrlEncoded.get("file_kind").getOrElse(Seq(""))(0);
            val parent_id = request.body.asFormUrlEncoded.get("parent_id").getOrElse(Seq(""))(0);
            val num = request.body.asFormUrlEncoded.get("num").getOrElse(Seq("0"))(0).toInt;
            val grades = request.body.asFormUrlEncoded.get("grades").getOrElse(Seq(""))(0)
            val gradesParse = grades.filter(_.isDigit).map(_.toString.toInt).toList

            val uID = UUID.randomUUID()

            val bucketName = ConfigFactory.load().as[String]("s3.bucketName")
            val bucket = S3(bucketName)

            val byteArr = FileUtils.readFileToByteArray(fileFromDisk)
            val result = bucket add BucketFile(fileUrl, file.contentType.getOrElse("text/plain"), byteArr, None, None)
            result.flatMap {
              unit =>
                {
                  val awsFileUrl = bucket.s3.url(bucketName, fileUrl)
                  logger.debug(s"Aws result $unit")

                  if (!only_upload) {

                    val curDoc = new Document(id = Some(uID), title = title, short_description = Some(""), user_id = user_id, description = desc, path = awsFileUrl, file_kind = Some(file_kind), school_kind = if (school_kind == "") None else Some(school_kind), parent_type = parent_type, parent_id = Some(parent_id), grades = Some(gradesParse), num = Some(num))

                    documentService.insert(curDoc).map {
                      res =>
                        res.inError match {
                          case true => {
                            InternalServerError(Json.obj("res" -> s" ${res.message}  code: ${res.code}"))
                          }
                          case false => {
                            Created(Json.obj("res" -> Json.obj("id" -> uID.toString, "path" -> awsFileUrl)))
                          }
                        }
                    }

                  } else {
                    Future.successful(Created(Json.obj("res" -> Json.obj("path" -> awsFileUrl))))
                  }

                }
            }.recover {
              case S3Exception(status, code, message, originalXml) =>
                {
                  logger.error("Error: " + message)
                  BadRequest(Json.obj("result" -> "main logo error: missing file or existing file"))
                }
            }

          }
      }.getOrElse {
        Future.successful(BadRequest(Json.obj("result" -> "main logo error: missing file or existing file")))
      }
  }

  def uploadOnly(parent_type: String) = Action.async(parse.multipartFormData) {
    implicit request =>
      request.body.file("file").map {
        file =>
          {
            val currentPath = application.path.getPath
            val filename = file.filename.replace(' ', '_')
            val filePath = currentPath + "/documents/" + parent_type + "/" + filename;
            val fileUrl = "download/documents/" + parent_type + "/" + filename;
            val contentType = file.contentType
            val dir = new File(currentPath + "/documents/" + parent_type)
            dir.mkdirs()
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

                  Future.successful(Created(Json.obj("res" -> Json.obj("path" -> awsFileUrl))))
                }
            }.recover {
              case S3Exception(status, code, message, originalXml) =>
                {
                  logger.error("Error: " + message)
                  BadRequest(Json.obj("result" -> "main logo error: missing file or existing file"))
                }
            }

          }
      }.getOrElse {
        Future.successful(BadRequest(Json.obj("result" -> "main logo error: missing file or existing file")))
      }
  }

  def download(filename: String) = Action {
    implicit request =>
      {
        logger.debug(s"DOWNLOAD ${application.path.getAbsolutePath + "/" + filename}")
        Ok.sendFile(new java.io.File(application.path.getAbsolutePath + "/" + filename))
      }
  }

}

