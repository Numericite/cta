package utils.emailing

/**
 * Created by Clément Lelong on 23/06/16.
 */
import model.mercure.daos.user.User
import play.api.Logger
import play.api.i18n.Messages
import play.twirl.api.Html
import utils.mercure.mail.MailService
import views.html.mails

object Emailing {

  implicit def html2String(html: Html): String = html.toString

  val logger = Logger("CC." + this.getClass.getSimpleName);

  def sendJokerLink(email: String, firstName: String, activity_id: String, token_id: String, user: User)(implicit ms: MailService, m: Messages) = {
    ms.sendEmailAsync(email)(
      subject = Messages("web.mail.joker.subject", user.firstName),
      bodyHtml = mails.joker(firstName, activity_id, token_id, user.firstName),
      bodyText = mails.joker_txt(firstName, activity_id, token_id, user.firstName)
    )
  }

}
