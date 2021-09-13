package models.daos.faq

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class Faq(
  id: Option[UUID] = None,
  title: String,
  answer: String,
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())

)

object FaqFormats {
  implicit val faqFormat = Json.format[Faq];
}
