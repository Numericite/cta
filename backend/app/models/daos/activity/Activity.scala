package models.daos.activity

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class Activity(
  id: Option[UUID] = None,
  name: String,
  desc: String,
  num: Int,
  slug: String,
  status_name: Option[String],
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())

)

object ActivityFormats {
  implicit val activityFormat = Json.format[Activity];
}
