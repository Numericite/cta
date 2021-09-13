package models.daos.version

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class Version(
  id: Option[UUID] = None,
  name: String,
  isDefault: Boolean,
  kind: String, // 'file' / 'activity'
  parent_id: String,
  created_date: Option[DateTime] = Some(new DateTime())
)

object VersionFormats {
  implicit val versionFormat = Json.format[Version];
}
