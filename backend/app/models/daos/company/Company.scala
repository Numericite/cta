package models.daos.company

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class Company(
  id: Option[UUID] = None,
  name: String,
  description: Option[String],
  kind: String, // company | association | collectivity | town-hall
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())

)

object CompanyFormats {
  implicit val partnerFormat = Json.format[Company];
}
