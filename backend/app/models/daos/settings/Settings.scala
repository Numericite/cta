package models.daos.settings

/**
 * Created by madalien on 12/19/16.
 */

import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json.Json
import reactivemongo.bson.BSONDocument

case class OnisepData(
  token: String
)

case class Settings(
  id: Option[UUID] = None,
  onisep: OnisepData,
  extra: Option[BSONDocument],
  updated_date: Option[DateTime] = Some(new DateTime())
)

object SettingsFormats {
  import reactivemongo.play.json.BSONFormats._
  implicit val onisepDataFormat = Json.format[OnisepData];
  implicit val settingsFormat = Json.format[Settings];
}
