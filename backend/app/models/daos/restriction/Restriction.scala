package models.daos.restriction

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class Restriction(
  id: Option[UUID] = None,
  exploration_type_id: String,
  school_id: Option[String],
  grade: Option[Int],
  classroom_id: String,
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())
)

object RestrictionFormats {
  implicit val restrictionFormat = Json.format[Restriction];
}
