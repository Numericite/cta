package models.daos.module.activity

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class ModuleActivity(
  id: Option[UUID] = None,
  module_id: String,
  name: String,
  description: String,
  path: Option[String],
  file_name: Option[String],
  num: Option[Int],
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())

)

object ModuleActivityFormats {
  implicit val moduleActivityFormat = Json.format[ModuleActivity];
}
