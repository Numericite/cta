package models.daos.loginLog

import org.joda.time.DateTime
import play.api.libs.json.Json

import java.util.UUID

/**
 * Created by Clément Lelong O8/05/17
 */
case class LoginLog(
  id: Option[UUID] = None,
  user_id: String,
  created_date: Option[DateTime] = Some(new DateTime())
)

object LoginLogFormats {
  implicit val loginLogFormat = Json.format[LoginLog];
}

