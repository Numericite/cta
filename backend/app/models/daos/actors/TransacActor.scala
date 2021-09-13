package models.daos.actors

/**
 * Created by Clément Lelong on 09/12/15.
 */
import akka.actor.{ ActorSystem, Actor, ActorRef, Props }
import play.api.Logger
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.libs.json.JsValue

class TransacActor(userID: String, event: String, outChannel: ActorRef, transacSupervisor: ActorRef) extends Actor {

  val log = Logger("CC." + this.getClass.getSimpleName);

  override def preStart() = {
    transacSupervisor ! Join(userID, event, outChannel)
  }

  override def postStop() = {
    log.debug(s"KILL transac actor -> [ $userID ]")
    transacSupervisor ! Quit(userID, event, outChannel)
  }

  def receive = {
    case json: JsValue => {
      val sendTo = (json \ "userID").asOpt[String].getOrElse("")
      val msg = (json \ "text").asOpt[String].getOrElse("")
      transacSupervisor ! Talk(sendTo, msg, event)
    }
  }

}

object TransacActor {
  lazy val transacSupervisor = ActorSystem().actorOf(Props[TransacSupervisor])
  def props(userID: String, event: String, outChannel: ActorRef) = {
    Props(new TransacActor(userID, event, outChannel, transacSupervisor))
  }
}
