package model.mercure.daos.user

import java.util.UUID

import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.api.{ Identity, LoginInfo }
import org.joda.time.DateTime
import play.api.libs.json.Json
import reactivemongo.bson.BSONDocument

/**
 * The user object.
 *
 * @param userID The unique ID of the user.
 * @param loginInfo The linked login info.
 */
case class User(
  userID: Option[UUID] = None,
  loginInfo: Option[LoginInfo] = None,
  roles: List[String],
  email: String,
  sex: String,
  config: Option[BSONDocument],
  firstName: String,
  lastName: String,
  active: Option[Boolean] = None,
  wishlist: Option[List[String]] = None,
  newsletter: Option[Boolean] = None,
  avatar_path: Option[String] = None,
  password: Option[String] = None,
  password_info: Option[PasswordInfo] = None,
  created_date: Option[DateTime] = Some(new DateTime()),
  updated_date: Option[DateTime] = Some(new DateTime())
) extends Identity

/**
 * The companion object.
 */
object User {
  /**
   * Converts the [User] object to Json and vice versa.
   */

  import reactivemongo.play.json.BSONFormats._
  implicit val passInfoFormat = Json.format[PasswordInfo]
  implicit val jsonFormat = Json.format[User]

}
