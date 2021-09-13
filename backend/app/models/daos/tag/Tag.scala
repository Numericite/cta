
/**
 * Created by Clément Lelong on 24/09/16.
 */
package models.daos.tag

import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json.Json

case class Tag(
  id: Option[UUID] = None,
  name: String,
  slug: String,
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())
)

object TagFormats {
  implicit val tagFormat = Json.format[Tag];
}
