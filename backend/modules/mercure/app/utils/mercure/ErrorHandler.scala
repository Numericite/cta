package utils.mercure

import javax.inject.Inject

import com.mohiva.play.silhouette.api.actions.SecuredErrorHandler
import play.api.http.DefaultHttpErrorHandler
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.json.Json
import play.api.mvc.Results._
import play.api.mvc.{ RequestHeader, Result }
import play.api.routing.Router
import play.api.{ Configuration, OptionalSourceMapper, UsefulException }

import scala.concurrent.Future

/**
 * A secured error handler.
 */
class ErrorHandler @Inject() (
  env: play.api.Environment,
  config: Configuration,
  sourceMapper: OptionalSourceMapper,
  val messagesApi: MessagesApi,
  router: javax.inject.Provider[Router]
)
    extends DefaultHttpErrorHandler(env, config, sourceMapper, router)
    with SecuredErrorHandler with I18nSupport {

  /**
   * Called when a user is not authenticated.
   *
   * As defined by RFC 2616, the status code of the response should be 401 Unauthorized.
   *
   * @param request The request header.
   * @return The result to send to the client.
   */
  override def onNotAuthenticated(implicit request: RequestHeader): Future[Result] = {
    Future.successful(Unauthorized(Json.toJson("You need to login.")))
  }

  override def onProdServerError(request: RequestHeader, exception: UsefulException) = {
    Future.successful(
      InternalServerError("A Mad prod server error occurred: " + exception.getMessage)
    )
  }

  override def onServerError(request: RequestHeader, exception: Throwable) = {
    Future.successful(
      InternalServerError("A Mad server error occurred: " + exception.getMessage)
    )
  }

  /**
   * Called when a user is authenticated but not authorized.
   *
   * As defined by RFC 2616, the status code of the response should be 403 Forbidden.
   *
   * @param request The request header.
   * @return The result to send to the client.
   */

  override def onNotAuthorized(implicit request: RequestHeader): Future[Result] = {
    Future.successful(Forbidden(Json.toJson("You're note authorized.")))
  }
}
