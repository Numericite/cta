package models.daos.course

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class ActivityHelper(
  id: String,
  version_id: String
)

case class ChildChapter(
  name: String,
  slug: String,
  kind: String, // 'activities' / 'tabs'
  display: Boolean,
  adminDisplay: Boolean,
  activities: Option[List[ActivityHelper]],
  tab_ids: Option[List[Int]]
)

case class Chapter(
  name: String,
  slug: String,
  display: Boolean,
  adminDisplay: Boolean,
  domain_ids: Option[List[String]],
  children: List[ChildChapter]
)

case class Course(
  id: Option[UUID] = None,
  name: String,
  chapters: List[Chapter],
  text_version_name: String,
  created_date: Option[DateTime] = Some(new DateTime())

)

object CourseFormats {
  implicit val activityHelperFormat = Json.format[ActivityHelper];
  implicit val childChapterFormat = Json.format[ChildChapter];
  implicit val chapterFormat = Json.format[Chapter];
  implicit val courseFormat = Json.format[Course];
}

