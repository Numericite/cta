package models.daos.module

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class Module(
  id: Option[UUID] = None,
  name: String,
  description: String,
  exploration_type_ids: Option[List[String]],
  status_name: Option[String],
  num: Int,
  help: Option[String],
  grade: Option[Int],
  school_type: Option[String], // middle-school, high-school-new, high-school-pro-new
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())

)

object ModuleFormats {
  implicit val moduleFormat = Json.format[Module];
}
