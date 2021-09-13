package models.daos.file

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class File(
  id: Option[UUID] = None,
  name: String,
  kind: String, // 'file' / 'folder'
  slug: String,
  parent_id: Option[String],
  created_date: Option[DateTime] = Some(new DateTime())

)

object FileFormats {
  implicit val fileFormat = Json.format[File];
}
