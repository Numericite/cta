package models.daos.domain

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class Domain(
  id: Option[UUID] = None,
  name: String,
  description: Option[String],
  img_path: Option[String],
  kind: String, // activity-area | stream | action-verb
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())

)

object DomainFormats {
  implicit val domainFormat = Json.format[Domain];
}
