package models.daos.notification

/**
 * Created by Clément Lelong O8/05/17
 */

import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._
import reactivemongo.bson.BSONDocument

case class Notification(
  id: Option[UUID] = None,
  user_id: String,
  value: Int,
  config: Option[BSONDocument],
  updated_date: Option[DateTime] = Some(new DateTime())
)

object NotificationFormats {
  import reactivemongo.play.json.BSONFormats._
  implicit val notificationFormat = Json.format[Notification];
}
