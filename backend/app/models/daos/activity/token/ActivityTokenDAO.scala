package models.daos.activity.token

import model.mercure.daos.CommonAttributes
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

/**
 * Created by Clément Lelong on 23/05/16.
 */
trait ActivityTokenDAO extends CommonAttributes {
  def insert(doc: ActivityToken): Future[WriteResult]
  def findAndModify(selector: BSONDocument, modifier: ActivityToken, fields: BSONDocument): Future[Option[ActivityToken]]
  def getList(page: Int, numberPerPage: Int): Future[List[ActivityToken]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("created_date" -> -1)): Future[Option[ActivityToken]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("created_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[ActivityToken]]
  def count(selector: JsObject): Future[Int]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}
