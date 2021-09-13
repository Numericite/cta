package models.daos.school.report

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class Report(
  specialities: Option[List[String]],
  school: Option[String],
  entry_conditions: Option[String]
)

case class SchoolReport(
  id: Option[UUID] = None,
  stream_id: String,
  user_id: String,
  schools: List[Report],
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())

)

object SchoolReportFormats {
  implicit val reportFormat = Json.format[Report];
  implicit val schoolReportFormat = Json.format[SchoolReport];
}
