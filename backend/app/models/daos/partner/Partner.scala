package models.daos.partner

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class Partner(
  id: Option[UUID] = None,
  name: String,
  img_path: String,
  slug: Option[String],
  school_ids: Option[List[String]],
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())

)

object PartnerFormats {
  implicit val partnerFormat = Json.format[Partner];
}
