package controllers

/**
 * Created by Clément Lelong on 10/12/15.
 */

import javax.inject.Inject

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.mohiva.play.silhouette.api.{ HandlerResult, Silhouette }
import models.daos.actors.ChatActor
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import utils.mercure.DefaultEnv

import scala.concurrent.Future

class BasicActorCtrl @Inject() (
    val messagesApi: MessagesApi,
    implicit val mat: Materializer,
    implicit val actorSystem: ActorSystem,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {

  val log = Logger("CC." + this.getClass.getSimpleName);

  def send(eventName: String, username: String, token: String) = WebSocket.acceptOrResult[JsValue, JsValue] {
    request =>
      {
        //log.debug(s"new query string:  ${request.getQueryString("X-Auth-Token")}")
        val tmpRequest = request.copy(headers = new Headers(Seq("X-Auth-Token" -> token)))
        implicit val req = Request(tmpRequest, AnyContentAsEmpty)
        silhouette.SecuredRequestHandler { securedRequest =>
          Future.successful(HandlerResult(Ok, Some(securedRequest.identity)))
        }.map {
          case HandlerResult(r, Some(user)) => {
            Right(ActorFlow.actorRef(ChatActor.props(username, eventName, _)))
          }
          case HandlerResult(r, None) => {
            Left(r)
          }
        }
      }
  }
}

