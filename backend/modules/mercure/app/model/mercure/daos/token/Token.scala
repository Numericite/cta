package model.mercure.daos.token

import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json.Json

/**
 * Created by madalien on 09/06/16.
 */

case class Token(
  id: Option[UUID] = None,
  entity: String,
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())
)

object Token {
  implicit val tokenFormat = Json.format[Token];
}
