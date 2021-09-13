package models.daos.actors

/**
 * Created by Coulibaly Mamadou on 04/01/16.
 */

import akka.actor._
import play.api.Logger
import play.api.libs.json.{ JsValue, JsString, Json }

import scala.collection.mutable

case class JoinChat(username: String, event: String, userActor: ActorRef)
case class QuitChat(username: String, event: String, userActor: ActorRef)
case class TalkChat(username: String, text: JsValue, event: String)

class ChatSupervisor extends Actor {

  val actorRefsByevents: mutable.Map[String, mutable.Set[ActorRef]] = mutable.Map[String, mutable.Set[ActorRef]]()
  val log = Logger("CC." + this.getClass.getSimpleName);
  //context.setReceiveTimeout(1000 milliseconds)
  override def receive: Receive = {
    case JoinChat(username, event, userActor) => {
      actorRefsByevents(event) = actorRefsByevents.getOrElse(event, mutable.Set(userActor))
      actorRefsByevents(event) += userActor
      log.debug(s"SUBSCRIBED TO TCHAT [ $username ]")
      broadcastMessage("Bot", Json.toJson("has joined this channel"), event, actorRefsByevents(event).size)
    }

    case TalkChat(username, text, event) => {
      broadcastMessage(username, text, event, actorRefsByevents(event).size)
    }

    case QuitChat(username, event, userActor) => {
      log.debug(s"HAS LEFT THE CHAT [$username]")
      log.debug(s"OLD NB USERS [ ${actorRefsByevents.size} ]")
      //if( actorRefsByevents.size > 0 ) {
      //actorRefsByevents(event) -= userActor
      val remainingUsers = actorRefsByevents.get(event) match {
        case Some(users) => {
          users -= userActor
          users.size
        }
        case None => 0
      }
      //}
      if (remainingUsers > 0)
        broadcastMessage("Bot", Json.toJson(s"has left the conversation"), event, remainingUsers)
      else {
        actorRefsByevents.remove(event)
        broadcastMessage("Bot", Json.toJson("has left the conversation"), event, 0)
      }
      log.debug(s" NEW NB USERS [${actorRefsByevents.size}]\n\n")
      userActor ! PoisonPill
    }
  }

  def broadcastMessage(userID: String, text: JsValue, event: String, connected: Int): Unit = {
    val msg = Json.obj(
      "username" -> JsString(userID),
      "message" -> Json.toJson(text),
      "event" -> event,
      "connected" -> connected
    )
    log.debug(s"connected [$connected]")
    if (connected != 0) {
      actorRefsByevents(event).foreach {
        member => member ! msg
      }
    }
  }

}
