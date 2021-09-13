package models.daos.notification

/**
 * Created by Clément Lelong O8/05/17
 */
import model.mercure.daos.CommonAttributes
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

trait NotificationDAO extends CommonAttributes {
  def insert(doc: Notification): Future[WriteResult]
  def findAndModify(selector: BSONDocument, modifier: Notification, fields: BSONDocument): Future[Option[Notification]]
  def getList(page: Int, numberPerPage: Int): Future[List[Notification]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Notification]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Notification]]
  def count(selector: JsObject): Future[Int]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}
