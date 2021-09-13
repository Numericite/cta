package controllers.cta

/**
 * Created by Clément Lelong O8/05/17
 */
import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.typesafe.config.ConfigFactory
import fly.play.s3.{ BucketFile, S3, S3Exception }
import models.daos.experience.Experience
import net.ceedubs.ficus.Ficus._
import org.apache.commons.io.FileUtils
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.experience.ExperienceService
import utils.mercure.DefaultEnv
import utils.mercure.auth.{ IsOwner, WithRoles }

import scala.concurrent.Future

class ExperienceCtrl @Inject() (
    val messagesApi: MessagesApi,
    val experienceService: ExperienceService,
    implicit val application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);

  import models.daos.experience.ExperienceFormats._

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Experience]) {
    implicit request =>
      {
        val experience = request.body
        val uid = UUID.randomUUID()

        val docWithId = experience.copy(id = Some(uid), updated_date = Some(new DateTime()))
        experienceService.insert(docWithId).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("Adminitrator")).async(parse.json[Experience]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        experienceService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldExperience =>
            {
              mayOldExperience match {
                case Some(oldExperience: Experience) => {
                  Ok(Json.obj("res" -> "Experience successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating experience"))
              }
            }
        }
      }
  }

  def updateMine(id: String, user_id: String) = silhouette.SecuredAction(IsOwner(user_id)).async(parse.json[Experience]) {
    implicit request =>
      {
        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id, "user_id" -> user_id);
        experienceService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldExperience =>
            {
              mayOldExperience match {
                case Some(oldExperience: Experience) => {
                  Ok(Json.obj("res" -> "Experience successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating experience"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        experienceService.remove(query).map {
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

  def removeMineByIds(ids: List[String], user_id: String) = silhouette.SecuredAction(IsOwner(user_id)).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids), "user_id" -> user_id)
        experienceService.remove(query).map {
          res =>
            res.inError match {
              case true => {

                InternalServerError(Json.obj("res" -> s" ${res.message}  code: ${res.code}"))
              }
              case false => {
                logger.info(s"${res}")
                Ok(Json.obj("res" -> s" ${res.message} Remove Done"))
              }
            }
        }
      }
  }

  def getList(
    page: Int,
    numberPerPage: Int,
    user_id: Option[String],
    kind: Option[String]
  ) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        var query = BSONDocument()

        if (user_id.isDefined) {
          query ++= BSONDocument("user_id" -> user_id)
        }

        if (kind.isDefined) {
          query ++= BSONDocument("kind" -> kind)
        }

        experienceService.find(query, page = page, numberPerPage = numberPerPage) map {
          experienceArr => Ok(Json.toJson(experienceArr))
        }
      }
  }

  def count() = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = Json.obj()

        experienceService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        experienceService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          experienceArr => Ok(Json.toJson(experienceArr))
        }
      }
  }

}
