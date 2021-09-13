package controllers.cta

/**
 * Created by Clément Lelong on 27/05/16.
 */

import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import models.daos.activity.token.ActivityToken
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.activity.token.ActivityTokenService
import services.mercure.user.UserService
import utils.emailing.Emailing
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles
import utils.mercure.mail.MailService

import scala.concurrent.Future

class ActivityTokenCtrl @Inject() (
    val messagesApi: MessagesApi,
    val activityTokenService: ActivityTokenService,
    val userService: UserService,
    val mailService: MailService,
    implicit val application: play.api.Application,
    passwordHasher: PasswordHasher,
    authInfoRepository: AuthInfoRepository,

    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("BP." + this.getClass.getSimpleName);
  implicit val ms: MailService = mailService;

  import models.daos.activity.token.ActivityTokenFormats._

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[ActivityToken]) {
    request =>
      {
        val uid = UUID.randomUUID()
        val activityToken = request.body.copy(id = Some(uid), updated_date = Some(new DateTime()), created_date = Some(new DateTime()))
        activityTokenService.insert(activityToken).map {
          res =>
            res.inError match {
              case true => {
                InternalServerError(Json.obj("res" -> s" ${res.message}  code: ${res.code}"))
              }
              case false => {
                val userQuery = BSONDocument("userID" -> activityToken.user_id)
                userService.find(userQuery) map {
                  userArr =>
                    Emailing.sendJokerLink(activityToken.email, activityToken.config.get.getAsTry[String]("firstName").getOrElse(""), activityToken.activity_id, uid.toString, userArr(0))
                }
                Created(Json.obj("res" -> Json.obj("id" -> uid.toString)))
              }
            }
        }
      }
  }

  def getByUserId(user_id: String, activity_id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    request =>
      {
        val query = BSONDocument("user_id" -> user_id, "activity_id" -> activity_id)
        activityTokenService.find(query, page = 1, numberPerPage = 1000) map {
          activityTokenArr => Ok(Json.toJson(activityTokenArr))
        }
      }
  }

  def checkValidity(activity_id: String, token_id: String) = Action.async {
    request =>
      {
        val query = BSONDocument("id" -> token_id, "activity_id" -> activity_id, "status_name" -> "open")
        activityTokenService.find(query) map {
          tokenArr =>
            {
              if (tokenArr.length > 0) {
                val token = tokenArr(0)
                Ok(Json.obj("res" -> tokenArr(0)))
              } else {
                NotFound(Json.obj("res" -> "Not a valid token"))
              }
            }
        }
      }
  }

  def validateToken(token_id: String) = Action.async(parse.json) {
    implicit request =>
      {
        (request.body \ "choice_ids").validate[List[String]].fold(
          error => { Future.successful(BadRequest(s"error")) },
          choice_ids => {
            val query = BSONDocument("id" -> token_id)
            val modifier = BSONDocument(
              "$set" ->
                BSONDocument(
                  "status_name" -> "done",
                  "config.choice_ids" -> choice_ids
                )
            )
            activityTokenService.update(query, modifier) map {
              res =>
                {
                  res.inError match {
                    case true => {
                      InternalServerError(Json.obj("res" -> s" ${res.message}  code: ${res.code}"))
                    }
                    case false => {
                      Ok(Json.obj("res" -> "choices saved"))
                    }
                  }
                }
            }
          }
        )

      }
  }

}
