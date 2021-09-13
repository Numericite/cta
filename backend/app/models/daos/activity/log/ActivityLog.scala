package models.daos.activity.log

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._
import reactivemongo.bson.BSONDocument

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class ActivityLog(
  id: Option[UUID] = None,
  user_id: String,
  activity_id: String,
  selector_ids: Option[List[String]],
  selection_ids: Option[List[String]],
  choice_ids: Option[List[String]],
  status_name: String,
  config: Option[BSONDocument],
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())

)

object ActivityLogFormats {
  import reactivemongo.play.json.BSONFormats._
  implicit val activityLogFormat = Json.format[ActivityLog];
}
