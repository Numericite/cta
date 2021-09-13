package models.daos.meeting

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class Meeting(
  id: Option[UUID] = None,
  name: String,
  kind: String, // school | structure
  description: Option[String],
  school_id: Option[String],
  nb_parent_informed: Option[Int],
  nb_structure_aware: Option[Int],
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())
)

object MeetingFormats {
  implicit val partnerFormat = Json.format[Meeting];
}
