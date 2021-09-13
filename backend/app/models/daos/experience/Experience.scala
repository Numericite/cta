package models.daos.experience

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class Experience(
  id: Option[UUID] = None,
  actions: Option[String],
  steps: Option[String],
  feedback: Option[String],
  date: Option[DateTime],
  domain_id: String,
  user_id: String,
  kind: String, // "domain" : "stream"
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())

)

object ExperienceFormats {
  implicit val experienceFormat = Json.format[Experience];
}
