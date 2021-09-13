package models.daos.association

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class Association(
  id: Option[UUID] = None,
  name: String,
  partner_slug: String,
  img_path: Option[String],
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())

)

object AssociationFormats {
  implicit val associationFormat = Json.format[Association];
}
