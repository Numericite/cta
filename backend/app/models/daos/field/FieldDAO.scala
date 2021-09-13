package models.daos.field

import model.mercure.daos.CommonAttributes
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

/**
 * Created by Clément Lelong on 23/05/16.
 */
trait FieldDAO extends CommonAttributes {
  def insert(doc: Field): Future[WriteResult]
  def findAndModify(selector: BSONDocument, modifier: Field, fields: BSONDocument): Future[Option[Field]]
  def getList(page: Int, numberPerPage: Int): Future[List[Field]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1)): Future[Option[Field]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Field]]
  def count(selector: JsObject): Future[Int]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}
