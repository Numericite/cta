package models.daos.document

import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json.Json

/**
 * Created by madalien on 23/05/16.
 */
case class Document(
  id: Option[UUID] = None,
  title: String,
  short_description: Option[String],
  path: String,
  user_id: String,
  file_kind: Option[String],
  parent_type: String,
  school_kind: Option[String] = None,
  parent_id: Option[String],
  grades: Option[List[Int]],
  description: String,
  num: Option[Int],
  updated_date: Option[DateTime] = Some(new DateTime())
)

object Document {
  implicit val documentFormats = Json.format[Document]
}