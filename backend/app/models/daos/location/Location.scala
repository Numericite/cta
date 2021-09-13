package models.daos.location

import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json.Json

/**
 * Created by Clément Lelong O8/05/17
 */
case class Location(
  id: Option[UUID],
  state: String,
  city: String,
  zip_code: Option[String],
  district: String,
  street: Option[String],
  country: Option[String],
  latitude: Option[Double],
  longitude: Option[Double],
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())
)

object Location {
  implicit val locationFormat = Json.format[Location];
}
