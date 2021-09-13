package controllers

import java.util.UUID
import javax.inject.Inject
import _root_.services.mercure.user.UserService
import _root_.services.loginLog.LoginLogService
import com.mohiva.play.silhouette.api.Authenticator.Implicits.RichDateTime
import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import com.typesafe.config.ConfigFactory
import model.mercure.daos.user.User
import models.daos.loginLog.LoginLog
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import play.modules.reactivemongo.json._
import reactivemongo.api.DB
import reactivemongo.bson.BSONDocument
import utils.mercure.DefaultEnv

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.concurrent.Execution.Implicits._

class Application @Inject() (
    val messagesApi: MessagesApi,
    val userService: UserService,
    val loginLogService: LoginLogService,
    passwordHasher: PasswordHasher,
    authInfoRepository: AuthInfoRepository,

    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("MR." + this.getClass.getSimpleName);

  import models.daos.loginLog.LoginLogFormats._

  def main = Action {
    Ok(Json.obj("res" -> "CTA API V1"))
  }

  def index = silhouette.UserAwareAction.async { implicit request =>

    request.identity match {
      case Some(user) => {
        logger.debug(s"CURRENT USER [ ${user.firstName} ]\n")
        logger.info(s"CURRENT USER [ ${user.firstName} ]\n")

        for {
          userLoginLog <- loginLogService.find(BSONDocument("user_id" -> user.userID.get.toString))
        } yield {

          if (userLoginLog.nonEmpty) {
            val oldLoginLogDate = userLoginLog.head.created_date.get
            val timeDiff = new DateTime().getMillis - oldLoginLogDate.getMillis

            if ((timeDiff / 3600000) > 2) {
              loginLogService.insert(LoginLog.apply(Some(UUID.randomUUID()), user.userID.get.toString, Some(new DateTime())))
            }
          } else {
            loginLogService.insert(LoginLog.apply(Some(UUID.randomUUID()), user.userID.get.toString, Some(new DateTime())))
          }

          Ok(Json.toJson(user))

        }
      }
      case _ => {
        logger.debug(s"CURRENT USER [ None ]\n")
        Future.successful(Unauthorized(Json.toJson("No user session found")))
      }
    }
  }

}
