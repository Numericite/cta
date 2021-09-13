package models.daos.actors

/**
 * Created by Clément Lelong on 10/12/15.
 */
/**
 * Created by Clément Lelong on 09/12/15.
 */
import akka.actor._
import com.google.inject.Inject
import play.api.Logger
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.libs.json.JsValue

import scala.concurrent.duration._

/*
case class JoinChat(username: String, event: String)
case class QuitChat(username: String, event: String)
case class TalkChat(username: String, text: String, event: String)

case class JoinChat(username:String, event: String, userActor: ActorRef)
case class QuitChat(username: String, event: String, userActor: ActorRef)
case class TalkChat(username: String, text: String, event: String)
*/

class ChatActor(username: String, event: String, outChannel: ActorRef, roomSupervisor: ActorRef) extends Actor {

  val log = Logger("CC." + this.getClass.getSimpleName);

  override def preStart() = {
    roomSupervisor ! JoinChat(username, event, outChannel)
  }

  override def postStop() = {
    log.debug(s"KILL chat actor -> [ $username ]")
    roomSupervisor ! QuitChat(username, event, outChannel)
  }

  def receive = {
    case json: JsValue => {
      roomSupervisor ! TalkChat(username, (json \ "text").get, event)
    }
  }

}

object ChatActor {
  lazy val roomSupervisor = ActorSystem().actorOf(Props[ChatSupervisor])
  def props(username: String, event: String, outChannel: ActorRef) = {
    Props(new ChatActor(username, event, outChannel, roomSupervisor))
  }
}

