package model.mercure.daos.category

import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Created by madalien on 03/03/15.
 */

case class Category(
  id: Option[UUID] = None,
  name: String,
  color: String,
  `type`: Option[String] = Some("basic"),
  avatar_path: Option[String] = None,
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())
)

object Category {
  implicit val CategoriesFormat = Json.format[Category];
}

