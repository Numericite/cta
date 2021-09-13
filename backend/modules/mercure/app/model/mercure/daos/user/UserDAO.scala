package model.mercure.daos.user

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import model.mercure.daos.CommonAttributes
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

/**
 * Give access to the user object.
 */
trait UserDAO extends CommonAttributes {

  /**
   * Finds a user by its login info.
   *
   * @param loginInfo The login info of the user to find.
   * @return The found user or None if no user for the given login info could be found.
   */
  def find(loginInfo: LoginInfo, active: Option[Boolean]): Future[Option[User]]

  /**
   * Finds a user by its user ID.
   *
   * @param userID The ID of the user to find.
   * @return The found user or None if no user for the given ID could be found.
   */
  def find(userID: UUID): Future[Option[User]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), page: Int = 1, numberPerPage: Int = 25): Future[List[User]]

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(user: User): Future[User]
  def getByIds(userIds: List[String]): Future[Seq[User]]

  def findAndModify(selector: BSONDocument, modifier: User, fields: BSONDocument): Future[Option[User]]
  def getList(page: Int, numberByPage: Int): Future[List[User]]

  def update(selector: BSONDocument, modifier: BSONDocument): Future[Option[User]]
  def count(selector: BSONDocument): Future[Int]

  def aggregate(pipeLine: BSONArray): Future[JsObject]
}
