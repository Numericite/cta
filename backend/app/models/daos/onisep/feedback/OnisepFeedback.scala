package models.daos.onisep.feedback

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class OnisepFeedbackContent(
  information: String,
  feedback: String
)

case class OnisepFeedback(
  id: Option[UUID] = None,
  code_uai: String,
  school_name: String,
  user_id: String,
  school_content: Option[OnisepFeedbackContent],
  skills_required: Option[OnisepFeedbackContent],
  perspectives: Option[OnisepFeedbackContent],
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())

)

object OnisepFeedbackFormats {
  implicit val onisepFeedbackContentFormat = Json.format[OnisepFeedbackContent];
  implicit val onisepFeedbackFormat = Json.format[OnisepFeedback];
}
