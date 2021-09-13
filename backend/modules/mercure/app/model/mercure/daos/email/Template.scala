package model.mercure.daos.email

import java.util.UUID

import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.api.{ Identity, LoginInfo }
import org.joda.time.DateTime
import play.api.libs.json.Json
import reactivemongo.bson.BSONDocument

/**
 * The email object.
 * Created by madalien on 21/05/18.
 *
 */
case class Template(
  id: Option[UUID] = None,
  name: String,
  subject: String,
  tags: Option[List[String]] = None,
  content: String,
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())
)

object Template {
  implicit val templateFormat = Json.format[Template];
}

