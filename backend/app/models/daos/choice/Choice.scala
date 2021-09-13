package models.daos.choice

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class Choice(
  id: Option[UUID] = None,
  text: String,
  selection_id: String,
  domain_ids: List[String],
  img_path: Option[String],
  description: Option[String],
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())

)

object ChoiceFormats {
  implicit val choiceFormat = Json.format[Choice];
}
