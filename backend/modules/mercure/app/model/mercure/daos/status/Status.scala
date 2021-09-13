package model.mercure.daos.status

import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json.Json

/**
 * Created by madalien on 23/05/16.
 */
case class Status(
  id: Option[UUID] = None,
  name: String,
  color: Option[String] = None,
  label: Option[String],
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())
)

object Status {
  implicit val statusFormat = Json.format[Status];
}
