package services.mercure.user

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.impl.providers.CommonSocialProfile
import model.mercure.daos.CommonAttributes
import model.mercure.daos.user.User
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

/**
 * Handles actions to users.
 */
trait UserService extends IdentityService[User] with CommonAttributes {

  def retrieve(loginInfo: LoginInfo, active: Option[Boolean] = None): Future[Option[User]]

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(user: User): Future[User]

  /**
   * Saves the social profile for a user.
   *
   * If a user exists for this profile then update the user, otherwise create a new user with the given profile.
   *
   * @return The user for whom the profile was saved.
   */
  def save(profile: CommonSocialProfile): Future[User]

  /**
   * get near users
   */

  def getByIds(userIds: List[String]): Future[Seq[User]]
  def update(selector: BSONDocument, modifier: BSONDocument): Future[Option[User]]
  def count(selector: BSONDocument): Future[Int]

  def getList(page: Int, numberByPage: Int): Future[List[User]]
  def findAndModify(selector: BSONDocument, modifier: User, fields: BSONDocument): Future[Option[User]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), page: Int = 1, numberPerPage: Int = 25): Future[List[User]]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}
