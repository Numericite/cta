package models.daos.field

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

case class Field(
  id: Option[UUID] = None,
  parent_id: String,
  label: Option[String],
  kind: Option[String],
  component: Option[String],
  children: Option[String],
  validation: Option[Boolean],
  validationMin: Option[Int],
  placeholder: Option[String],
  sort_id: Int,
  optionsField: Option[List[BSONDocument]],
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())
)

object FieldFormats {
  import reactivemongo.play.json.BSONFormats._
  implicit val fieldFormat = Json.format[Field];
}
