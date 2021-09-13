package utils.mercure.mail

/**
 * Created by madalien on 31/05/16.
 */

import model.mercure.daos.user.User
import com.typesafe.config.ConfigFactory
import model.mercure.daos.email.Template
import play.api.i18n.Messages
import play.twirl.api.Html
import views.html.mercure.mails
import net.ceedubs.ficus.Ficus._
import reactivemongo.bson.BSONDocument
import services.mercure.email.TemplateService
import scala.concurrent.ExecutionContext.Implicits.global

object Mailer {

  implicit def html2String(html: Html): String = html.toString
  val subjectConf = ConfigFactory.load().getAs[String]("web.mail.welcome.subject")
  val siteUrlConf = ConfigFactory.load().getAs[String]("web.mail.siteUrl")
  val mailSignConf = ConfigFactory.load().getAs[String]("web.mail.sign")

  def welcome(user: User, identifier: String, pwd: String)(implicit ms: MailService, m: Messages, emailService: TemplateService) {

    val siteUrl = if (siteUrlConf.nonEmpty) siteUrlConf.get else Messages("web.mail.siteUrl")
    val mailSign = if (mailSignConf.nonEmpty) mailSignConf.get else Messages("web.mail.sign")

    //template tags

    val query = BSONDocument("name" -> "welcome")
    emailService.findOne(query).foreach {
      case Some(emailInfo: Template) => {

        val name = s"${user.firstName} ${user.lastName}"
        /*
        var emailBody = "".concat(emailInfo.content)
        val template = (name: String, identifier: String, pwd: String, siteUrl: String, mailSign: String) => s"${emailInfo.content.toString})"
        val msg = template(name, identifier, pwd, siteUrl, mailSign)
        */
        val emailBody = emailInfo.content.format(name: String, identifier: String, pwd: String, siteUrl: String, mailSign: String)
        ms.sendEmailAsync(user.email)(
          subject = emailInfo.subject,
          bodyHtml = emailBody,
          bodyText = emailBody
        )
      }
      case _ => {
        ms.sendEmailAsync(user.email)(
          subject = if (subjectConf.nonEmpty) subjectConf.get else Messages("web.mail.welcome.subject"),
          bodyHtml = mails.welcome(s"${user.firstName} ${user.lastName}", identifier, pwd, siteUrl, mailSign),
          bodyText = mails.welcomeTxt(s"${user.firstName} ${user.lastName}", identifier, pwd, siteUrl, mailSign)
        )
      }
    }

  }

  def forgotPassword(email: String, link: String)(implicit ms: MailService, m: Messages, emailService: TemplateService) {
    val siteUrl = if (siteUrlConf.nonEmpty) siteUrlConf.get else Messages("web.mail.siteUrl")
    val mailSign = if (mailSignConf.nonEmpty) mailSignConf.get else Messages("web.mail.sign")

    val query = BSONDocument("name" -> "password-reset")
    emailService.findOne(query).foreach {
      case Some(emailInfo: Template) =>
        val emailBody = emailInfo.content.format(
          siteUrl + link: String,
          mailSign: String
        )
        ms.sendEmailAsync(email)(
          subject = emailInfo.subject,
          bodyHtml = emailBody,
          bodyText = emailBody
        )

      case _ =>
        ms.sendEmailAsync(email)(
          subject = Messages("web.mail.forgotpwd.subject"),
          bodyHtml = mails.forgotPassword(email, link, siteUrl, mailSign),
          bodyText = mails.forgotPasswordTxt(email, link, siteUrl, mailSign)
        )
    }

  }

  def emailValidation(email: String, link: String, name: String)(implicit ms: MailService, m: Messages, emailService: TemplateService) {
    val siteUrl = if (siteUrlConf.nonEmpty) siteUrlConf.get else Messages("web.mail.siteUrl")
    val mailSign = if (mailSignConf.nonEmpty) mailSignConf.get else Messages("web.mail.sign")

    val query = BSONDocument("name" -> "validation")
    emailService.findOne(query).foreach {
      case Some(emailInfo: Template) =>
        val emailBody = emailInfo.content.format(
          name: String,
          siteUrl + link: String,
          mailSign: String
        )
        ms.sendEmailAsync(email)(
          subject = emailInfo.subject,
          bodyHtml = emailBody,
          bodyText = emailBody
        )

      case _ =>
        ms.sendEmailAsync(email)(
          subject = Messages("web.mail.validation.subject", siteUrl),
          bodyHtml = mails.emailValidation(name, link, siteUrl),
          bodyText = mails.emailValidationTxt(name, link, siteUrl)
        )
    }

  }

  def userActivated(user: User)(implicit ms: MailService, m: Messages) = {
    ms.sendEmailAsync(user.email)(
      subject = Messages("web.mail.activated.subject"),
      bodyHtml = mails.userActivated(user.firstName, user.lastName),
      bodyText = mails.userActivatedTxt(user.firstName, user.lastName)
    )
  }

  def userRefused(user: User)(implicit ms: MailService, m: Messages) = {
    ms.sendEmailAsync(user.email)(
      subject = Messages("web.mail.refused.subject"),
      bodyHtml = mails.userRefused(user.firstName, user.lastName),
      bodyText = mails.userRefusedTxt(user.firstName, user.lastName)
    )
  }

}
