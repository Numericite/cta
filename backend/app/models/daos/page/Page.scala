package models.daos.page

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class Page(
  id: Option[UUID] = None,
  name: String,
  version_id: String,
  status_name: String,
  texts: Option[List[String]],
  created_date: Option[DateTime] = Some(new DateTime())

)

object PageFormats {
  implicit val pageFormat = Json.format[Page];
}
