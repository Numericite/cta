package models.daos.selection

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class Selection(
  id: Option[UUID] = None,
  text: String,
  activity_id: String,
  version_id: String,
  color: Option[String],
  img_path: Option[String],
  description: Option[String],
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())

)

object SelectionFormats {
  implicit val selectionFormat = Json.format[Selection];
}
