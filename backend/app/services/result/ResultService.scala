package services.result

/**
 * Created by Clément Lelong on 08/05/17.
 */
import model.mercure.daos.CommonAttributes
import models.daos.result.Result
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ MultiBulkWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

trait ResultService extends CommonAttributes {
  def insert(doc: Result): Future[WriteResult]
  def insertMany(docs: List[Result]): Future[MultiBulkWriteResult]
  def getList(page: Int, numberPerPage: Int): Future[List[Result]]
  def findAndModify(selector: BSONDocument, modifier: Result, fields: BSONDocument): Future[Option[Result]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Result]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Result]]
  def count(selector: JsObject = Json.obj()): Future[Int]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}
