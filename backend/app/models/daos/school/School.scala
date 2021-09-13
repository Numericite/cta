package models.daos.school

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class School(
  id: Option[UUID] = None,
  name: String,
  kind: String, // middle-school / high-school / high-school-pro / ime / esat
  specificity: Option[String], // base / rep / rep-plus / qpv
  territory: Option[String], // urban / school-city / rural / pre
  course_id: Option[String],
  second_year_access_rate: Option[Int],
  img_path: Option[String],
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())

)

object SchoolFormats {
  implicit val schoolFormat = Json.format[School];
}
