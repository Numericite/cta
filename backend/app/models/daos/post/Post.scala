package models.daos.post

/**
 * Created by Clément Lelong on 23/05/16.
 */
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by Clément Lelong on 03/03/15.
 */

case class Post(
  id: Option[UUID] = None,
  title: String,
  content: String,
  status_name: String,
  author_id: Option[String],
  tag_ids: Option[List[String]],
  avatar_path: Option[String],
  isPinned: Option[Boolean],
  date: DateTime,
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())
)

object PostFormats {
  implicit val postFormat = Json.format[Post];
}
