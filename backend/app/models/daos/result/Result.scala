package models.daos.result

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class Result(
  id: Option[UUID] = None,
  choice_id: String,
  user_id: String,
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())

)

object ResultFormats {
  implicit val resultFormat = Json.format[Result];
}
