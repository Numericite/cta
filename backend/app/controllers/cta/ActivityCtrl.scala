package controllers.cta

/**
 * Created by Clément Lelong on 27/05/16.
 */

import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import models.daos.activity.Activity
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.activity.ActivityService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

import scala.concurrent.Future

class ActivityCtrl @Inject() (
    val messagesApi: MessagesApi,
    val activityService: ActivityService,
    implicit val application: play.api.Application,
    passwordHasher: PasswordHasher,
    authInfoRepository: AuthInfoRepository,

    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("BP." + this.getClass.getSimpleName);

  import models.daos.activity.ActivityFormats._

  def create = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[Activity]) {
    request =>
      {
        val uid = UUID.randomUUID()
        val activity = request.body.copy(id = Some(uid), updated_date = Some(new DateTime()), created_date = Some(new DateTime()))
        activityService.insert(activity).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[Activity]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        logger.debug(s"$elemToUpdate")
        activityService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldUsr =>
            {
              mayOldUsr match {
                case Some(oldUsr: Activity) => {
                  Ok(Json.obj("res" -> Json.toJson(oldUsr)))
                }
                case _ => InternalServerError(Json.obj("res" -> s"error while updating activity"))
              }
            }
        }
      }
  }

  def updateStatus(activity_id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json) {

    implicit request =>
      {
        (request.body \ "status_name").validate[String].fold(
          error => { Future.successful(BadRequest(s"error")) },
          status_name => {

            var fieldsToUpdate = BSONDocument(
              "status_name" -> status_name
            )

            val subModifier = BSONDocument(
              "$set" -> fieldsToUpdate
            )

            val selec = BSONDocument("id" -> activity_id);
            activityService.update(selec, subModifier) map {
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

  def getList(page: Int, numberPerPage: Int, status_names: Option[List[String]], name: Option[String]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        var query = BSONDocument()

        if (status_names.get.nonEmpty) {
          query ++= BSONDocument("status_name" -> BSONDocument("$in" -> status_names))
        }

        if (name.nonEmpty) {
          query ++= BSONDocument("name" -> BSONDocument("$regex" -> name.get, "$options" -> "i"))
        }

        activityService.find(query, page = page, numberPerPage = numberPerPage) map {
          subArr => Ok(Json.toJson(subArr))
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        activityService.remove(query).map {
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

  def getByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        activityService.find(query).map {
          subArr => Ok(Json.toJson(subArr))
        }
      }
  }

  def count(status_names: Option[List[String]], name: Option[String]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        var query = Json.obj()

        if (status_names.get.nonEmpty) {
          query ++= Json.obj("status_name" -> Json.obj("$in" -> status_names))
        }

        if (name.nonEmpty) {
          query ++= Json.obj("name" -> Json.obj("$regex" -> name.get, "$options" -> "i"))
        }

        activityService.count(query).map {
          nbActivities => Ok(Json.obj("res" -> nbActivities))
        }
      }
  }

}
