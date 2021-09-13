package models.daos.resource

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class Resource(
  id: Option[UUID] = None,
  name: String,
  link: String,
  description: String,
  img_path: Option[String],
  parent_type: String,
  embed_video_code: Option[String],
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())
)

object ResourceFormats {
  implicit val resourceFormat = Json.format[Resource];
}
