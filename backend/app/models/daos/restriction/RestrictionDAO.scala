package models.daos.restriction

import model.mercure.daos.CommonAttributes
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

/**
 * Created by Clément Lelong on 23/05/16.
 */
trait RestrictionDAO extends CommonAttributes {
  def insert(doc: Restriction): Future[WriteResult]
  def findAndModify(selector: BSONDocument, modifier: Restriction, fields: BSONDocument): Future[Option[Restriction]]
  def getList(page: Int, numberPerPage: Int): Future[List[Restriction]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1)): Future[Option[Restriction]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Restriction]]
  def count(selector: JsObject): Future[Int]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}
