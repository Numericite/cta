package models.daos.fieldlog

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._
import reactivemongo.bson.BSONDocument

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class FieldLog(
  id: Option[UUID] = None,
  field_id: String,
  user_id: String,
  child_id: String,
  child_type: String, // For User Or Exploration
  values: List[String],
  sort_id: Int,
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())

)

object FieldLogFormats {
  import reactivemongo.play.json.BSONFormats._
  implicit val fieldLogFormat = Json.format[FieldLog];
}
