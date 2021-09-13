package models.daos.lead

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class Lead(
  id: Option[UUID] = None,
  description: String,
  user_id: String,
  choice_ease: Option[Int],
  choice_ready: Option[Int],
  choice_able: Option[Int],
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())

)

object LeadFormats {
  implicit val leadFormat = Json.format[Lead];
}
