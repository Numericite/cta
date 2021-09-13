package models.daos.detail

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class Detail(
  id: Option[UUID] = None,
  question: String,
  answer: String,
  short_answer: String,
  domain_id: String,
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())

)

object DetailFormats {
  implicit val detailFormat = Json.format[Detail];
}
