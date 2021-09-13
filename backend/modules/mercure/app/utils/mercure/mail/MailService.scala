

package utils.mercure.mail

/**
 * Created by madalien on 30/05/16.
 */

import javax.inject.Inject

import akka.actor.ActorSystem
import com.google.inject.ImplementedBy
import com.typesafe.config.ConfigFactory
import play.api.Configuration
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.mailer._

import scala.concurrent.duration._

@ImplementedBy(classOf[MailServiceImpl])
trait MailService {
  def sendEmailAsync(recipients: String*)(subject: String, bodyHtml: String, bodyText: String): Unit
  def sendEmail(recipients: String*)(subject: String, bodyHtml: String, bodyText: String): Unit
}

class MailServiceImpl @Inject() (mailerClient: MailerClient, val conf: Configuration, val actorSystem: ActorSystem) extends MailService {

  lazy val from = ConfigFactory.load().getString("play.mailer.from")

  def sendEmailAsync(recipients: String*)(subject: String, bodyHtml: String, bodyText: String) = {
    actorSystem.scheduler.scheduleOnce(1000 milliseconds) {
      sendEmail(recipients: _*)(subject, bodyHtml, bodyText)
    }
  }
  def sendEmail(recipients: String*)(subject: String, bodyHtml: String, bodyText: String) =
    mailerClient.send(Email(subject, from, recipients, Some(bodyText), Some(bodyHtml)))

}

