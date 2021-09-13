package models.daos.result

import model.mercure.daos.CommonAttributes
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ MultiBulkWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

/**
 * Created by Clément Lelong on 23/05/16.
 */
trait ResultDAO extends CommonAttributes {
  def insert(doc: Result): Future[WriteResult]
  def insertMany(docs: List[Result]): Future[MultiBulkWriteResult]
  def findAndModify(selector: BSONDocument, modifier: Result, fields: BSONDocument): Future[Option[Result]]
  def getList(page: Int, numberPerPage: Int): Future[List[Result]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Result]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Result]]
  def count(selector: JsObject): Future[Int]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}
