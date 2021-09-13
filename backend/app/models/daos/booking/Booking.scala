package models.daos.booking

/**
 * Created by Clément Lelong on 23/05/16.
 */
import org.joda.time.DateTime
import play.api.libs.json._

import java.util.UUID

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class Booking(
  id: Option[UUID] = None,
  parent_id: String,
  status: String, // Booking : host / accepted / pending
  user_id: String,
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())
)

object BookingFormats {
  implicit val BookingFormat = Json.format[Booking];
}
