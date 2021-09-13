package models.daos.school.classroom

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class SchoolClassroom(
  id: Option[UUID] = None,
  name: String,
  school_id: String,
  grade: Int,
  course_id: Option[String],
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())

)

object SchoolClassroomFormats {
  implicit val schoolFormat = Json.format[SchoolClassroom];
}
