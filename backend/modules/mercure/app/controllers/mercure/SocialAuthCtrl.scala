package controllers.mercure

/**
 * Created by madalien on 01/08/16.
 */

import javax.inject.Inject
import _root_.services.mercure.user.UserService
import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.impl.providers._
import play.api.Logger
import play.cache.CacheApi
import play.api.i18n.{ I18nSupport, Messages, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc._
import reactivemongo.bson.BSONDocument
import utils.mercure.DefaultEnv

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps

/**
 * The social auth controller.
 *
 * @param messagesApi The Play messages API.
 * @param silhouette The Silhouette stack.
 * @param userService The user service implementation.
 * @param authInfoRepository The auth info service implementation.
 * @param socialProviderRegistry The social provider registry.
 */
class SocialAuthCtrl @Inject() (
    val messagesApi: MessagesApi,
    silhouette: Silhouette[DefaultEnv],
    userService: UserService,
    authInfoRepository: AuthInfoRepository,
    socialProviderRegistry: SocialProviderRegistry,
    cache: CacheApi
) extends Controller with I18nSupport {

  val logger = Logger("MR." + this.getClass.getSimpleName);
  /**
   * Authenticates a user against a social provider.
   *
   * @param provider The ID of the provider to authenticate against.
   * @return The result to display.
   */
  def fromCacheAuthenticate(provider: String) = Action.async { r =>
    cacheAuthTokenForOauth1(r) { implicit request =>
      (socialProviderRegistry.get[SocialProvider](provider) match {
        case Some(p: SocialProvider with CommonSocialProfileBuilder) =>
          p.authenticate().flatMap {
            case Left(result) => {
              Future.successful(result)
            }
            case Right(authInfo) => {

              for {
                profile <- p.retrieveProfile(authInfo)
                user <- userService.save(profile)
                authInfo <- {
                  authInfoRepository.save(profile.loginInfo, authInfo)
                }
                authenticator <- silhouette.env.authenticatorService.create(profile.loginInfo)
                token <- silhouette.env.authenticatorService.init(authenticator)
              } yield {
                silhouette.env.eventBus.publish(LoginEvent(user, request))
                Ok(Json.obj("token" -> token))
              }
            }
          }
        case _ => Future.failed(new ProviderException(s"Cannot authenticate with unexpected social provider $provider"))
      }).recover {
        case e: ProviderException =>
          logger.error("Unexpected provider error", e)
          Unauthorized(Json.obj("message" -> Messages("could.not.authenticate")))
      }
    }
  }

  /**
   * non cached auth
   */

  def authenticate(provider: String) = Action.async {
    implicit request =>
      (socialProviderRegistry.get[SocialProvider](provider) match {
        case Some(p: SocialProvider with CommonSocialProfileBuilder) =>
          p.authenticate().flatMap {
            case Left(result) => {
              Future.successful(result)
            }
            case Right(authInfo) => {

              p.retrieveProfile(authInfo) flatMap {
                profile =>
                  {
                    val userEmailQuery = BSONDocument("email" -> profile.email.getOrElse("emailnotexisting"), "loginInfo.providerID" -> BSONDocument("$ne" -> profile.loginInfo.providerID))

                    userService.find(userEmailQuery).flatMap {
                      case users =>
                        {
                          if (users.length > 0) {
                            Future.successful(
                              Conflict(
                                Json.obj("res" ->
                                  Json.obj(
                                    "messsage" -> "user already exists with another provider",
                                    "email" -> users(0).email,
                                    "provider" -> users(0).loginInfo.get.providerID
                                  ))
                              )
                            )
                          } else {
                            for {
                              user <- userService.save(profile)
                              authInfo <- authInfoRepository.save(profile.loginInfo, authInfo)
                              authenticator <- silhouette.env.authenticatorService.create(profile.loginInfo)
                              token <- silhouette.env.authenticatorService.init(authenticator)
                            } yield {
                              silhouette.env.eventBus.publish(LoginEvent(user, request))
                              Ok(Json.obj("token" -> token)).withCookies(Cookie("X-Auth-Token", token.toString))
                            }
                          }
                        }
                    }
                  }
              }
            }
          }
        case _ => Future.failed(new ProviderException(s"Cannot authenticate with unexpected social provider $provider"))
      }).recover {
        case e: ProviderException => {
          logger.error("Unexpected provider error", e)
          Unauthorized(Json.obj("message" -> Messages("could.not.authenticate")))
        }
      }

  }

  /**
   * Satellizer executes multiple requests to the same application endpoints for OAuth1.
   *
   * So this function caches the response from the OAuth provider and returns it on the second
   * request. Not nice, but it works as a temporary workaround until the bug is fixed.
   *
   * @param request The current request.
   * @param f The action to execute.
   * @return A result.
   * @see https://github.com/sahat/satellizer/issues/287
   */
  private def cacheAuthTokenForOauth1(request: Request[AnyContent])(f: Request[AnyContent] => Future[Result]): Future[Result] = {
    request.getQueryString("oauth_token") -> request.getQueryString("oauth_verifier") match {
      case (Some(token), Some(verifier)) => cache.get[Result](token + "-" + verifier) match {
        case result: Result => {

          logger.error(s"Stored in CACHEEEEE: $result")
          Future.successful(result)
        }
        case none => f(request).map {
          result =>
            {

              logger.error(s"Nothing in CACHEEEEE: $result none: $none")
              cache.set(token + "-" + verifier, result, 60)
              result
            }
        }
      }
      case _ => {

        logger.error(s"No CACHEEEEE!!!!!!")
        f(request)
      }
    }
  }
}
