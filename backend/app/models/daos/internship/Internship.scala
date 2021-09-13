package models.daos.internship

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class Internship(
  id: Option[UUID] = None,
  name: String,
  kind: String, // dt | dme
  description: Option[String],
  company_id: Option[String],
  school_ids: Option[List[String]],
  nb_participant_boy: Option[Int],
  nb_participant_girl: Option[Int],
  nb_immersions: Option[Int],
  nb_visits: Option[Int],
  satisfaction_rate: Option[Int],
  acquisition_operation_rate: Option[Int],
  acquisition_skills_rate: Option[Int],
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())

)

object InternshipFormats {
  implicit val internshipFormat = Json.format[Internship];
}
