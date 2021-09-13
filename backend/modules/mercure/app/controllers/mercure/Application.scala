package controllers.mercure

import java.util.UUID
import javax.inject.Inject

import _root_.services.mercure.user.UserService
import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import com.typesafe.config.ConfigFactory
import model.mercure.daos.user.User
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

class Application @Inject() (
  val messagesApi: MessagesApi,
  val userService: UserService,
  passwordHasher: PasswordHasher,
  authInfoRepository: AuthInfoRepository,

  silhouette: Silhouette[DefaultEnv]
)
    extends Controller with I18nSupport {
  val logger = Logger("MR." + this.getClass.getSimpleName);

  def index = silhouette.UserAwareAction.async { implicit request =>

    request.identity match {
      case Some(user) => {
        logger.debug(s"CURRENT USER [ ${user.firstName} ]\n")
        logger.info(s"CURRENT USER [ ${user.firstName} ]\n")
        Future.successful(Ok(Json.toJson(user)))
      }
      case _ => {
        logger.debug(s"CURRENT USER [ None ]\n")
        Future.successful(Unauthorized(Json.toJson("No user session found")))
      }
    }
  }

  def install = Action.async {
    userService.count(BSONDocument()).flatMap {
      count =>
        {
          count match {
            case count if (count == 0) => Future.successful(Ok(Json.obj("res" -> Json.obj("isOk" -> false))))
            case count if count > 0 => Future.successful(Ok(Json.obj("res" -> Json.obj("isOk" -> true, "nbCount" -> s"${count}"))))
            case _ => Future.successful(InternalServerError(Json.toJson(s"Error while counting")))
          }
        }
    }
  }

  import net.ceedubs.ficus.Ficus._
  def setup(secret: String) = Action.async(parse.json[User]) {
    implicit request =>
      {
        val secretFromFile = ConfigFactory.load().as[String]("play.install.secret")
        logger.debug(s"Install secret [ $secretFromFile]")

        if (secretFromFile != secret) {
          Future.successful(Forbidden(Json.obj("res" -> "wrong rights")))
        }

        val curUser = request.body
        val loginInfo = LoginInfo(CredentialsProvider.ID, curUser.email)
        logger.debug(s"SIGN UP [${curUser.lastName}] ${loginInfo.providerID} ${loginInfo.providerKey} $secretFromFile")
        userService.retrieve(loginInfo).flatMap {

          case Some(user) =>
            Future.successful(Conflict(Json.obj("res" -> "error user already exists")))

          case curUser.password if (curUser.password.isEmpty) =>
            Future.successful(BadRequest(Json.obj("res" -> "Please specify the password")))

          case None =>
            val authInfo = passwordHasher.hash(curUser.password.get)
            val user = curUser.copy(
              userID = Some(UUID.randomUUID()),
              loginInfo = Some(loginInfo),
              active = Some(true),
              password = None
            )
            for {
              // avatar <- avatarService.retrieveURL(data.email)
              user <- userService.save(user)
              authInfo <- authInfoRepository.add(loginInfo, authInfo)
              authenticator <- silhouette.env.authenticatorService.create(loginInfo)
              authenticator <- Future.successful(authenticator.copy(idleTimeout = Some(30 days), expirationDateTime = new DateTime().plusDays(30)))
              token <- silhouette.env.authenticatorService.init(authenticator)
              result <- silhouette.env.authenticatorService.embed(token, Ok(Json.obj("res" -> Json.obj("message" -> "Account created", "user" -> user, "token" -> token, "expirationDate" -> authenticator.expirationDateTime))))
            } yield {
              silhouette.env.eventBus.publish(SignUpEvent(user, request))
              silhouette.env.eventBus.publish(LoginEvent(user, request))
              result
            }
        }
      }
  }

  def getServerDate() = Action {
    val serverDate = java.time.LocalDateTime.now(java.time.ZoneOffset.UTC).toString
    Ok(Json.obj("date" -> serverDate))
  }
}
