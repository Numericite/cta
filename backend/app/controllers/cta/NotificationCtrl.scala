package controllers.cta

/**
 * Created by Clément Lelong O8/05/17
 */
import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import models.daos.notification.Notification
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.{ BSONArray, BSONDocument, BSONNull }
import services.notification.NotificationService
import services.mercure.user.UserService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

import scala.concurrent.Future

class NotificationCtrl @Inject() (
    val messagesApi: MessagesApi,
    val notificationService: NotificationService,
    application: play.api.Application,
    passwordHasher: PasswordHasher,
    authInfoRepository: AuthInfoRepository,
    userService: UserService,

    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);
  import models.daos.notification.NotificationFormats._

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Notification]) {
    implicit request =>
      {
        val notification = request.body
        val uid = UUID.randomUUID()

        val docWithId = notification.copy(id = Some(uid), updated_date = Some(new DateTime()))
        notificationService.insert(docWithId).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("Editor", "NormalUser")).async(parse.json[Notification]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        notificationService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldSub =>
            {
              mayOldSub match {
                case Some(oldSub: Notification) => {
                  Ok(Json.obj("res" -> "Notification successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating notification"))
              }
            }
        }
      }
  }

  def reset(user_id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json) {
    implicit request =>
      {
        (request.body \ "exchange_id").validate[String].fold(
          error => { Future.successful(BadRequest(s"no exchange id")) },
          exchange_id => {
            val elemToUpdate = BSONDocument(
              "$set" -> BSONDocument(
                "value" -> 0
              )
            )
            val selector = BSONDocument("user_id" -> user_id, "config.exchange_id" -> exchange_id)
            notificationService.update(selector, elemToUpdate) map {
              case res if (res.ok) => {
                Ok(Json.obj("res" -> s" ${res.message} reset Done"))
              }
              case err => {
                InternalServerError(Json.obj("res" -> s" ${err.message}  code: ${err.code}"))
              }
            }
          }
        )
      }
  }

  def getByUserId(user_id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = BSONDocument("user_id" -> user_id)
        notificationService.find(query, page = 1, numberPerPage = 1000).map {
          notificationsArr => Ok(Json.toJson(notificationsArr))
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        notificationService.remove(query).map {
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

  def removeByUserId(user_id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = BSONDocument("user_id" -> user_id)
        notificationService.remove(query).map {
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
