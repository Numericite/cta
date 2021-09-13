package models.daos.activity.token

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

case class ActivityToken(
  id: Option[UUID] = None,
  email: String,
  activity_id: String,
  status_name: String,
  user_id: String,
  config: Option[BSONDocument],
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())

)

object ActivityTokenFormats {
  import reactivemongo.play.json.BSONFormats._
  implicit val activityTokenFormat = Json.format[ActivityToken];
}
