package models.daos.explorationType

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class ExplorationType(
  id: Option[UUID] = None,
  name: String,
  description: String,
  status_name: Option[String],
  kind: Option[String],
  num: Option[Int],
  partner_slug: Option[String],
  association_id: Option[String],
  category: Option[String],
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())

)

object ExplorationTypeFormats {
  implicit val explorationTypeFormat = Json.format[ExplorationType];
}
