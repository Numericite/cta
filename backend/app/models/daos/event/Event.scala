package models.daos.event

/**
 * Created by Clément Lelong on 23/05/16.
 */
import org.joda.time.DateTime
import play.api.libs.json._

import java.util.UUID

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class Event(
  id: Option[UUID] = None,
  title: String,
  description: Option[String],
  startDate: DateTime,
  hours: String,
  endDate: Option[DateTime],
  kind: String, // planning
  kind_object: String, // planning : interview / sequence
  status: Option[String], // planning (interview) : pending / validate
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())
)

object EventFormats {
  implicit val EventFormat = Json.format[Event];
}
