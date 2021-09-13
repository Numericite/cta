package models.daos.exploration

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class Exploration(
  id: Option[UUID] = None,
  name: String,
  date: Option[DateTime],
  exploration_type_id: String,
  user_id: String,
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())

)

object ExplorationFormats {
  implicit val explorationFormat = Json.format[Exploration];
}
