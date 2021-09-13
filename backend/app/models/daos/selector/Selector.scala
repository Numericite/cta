package models.daos.selector

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class Selector(
  id: Option[UUID] = None,
  text: String,
  selection_id: String,
  num: Int,
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())

)

object SelectorFormats {
  implicit val selectorFormat = Json.format[Selector];
}
