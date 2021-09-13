package models.daos.exploration

import model.mercure.daos.CommonAttributes
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

/**
 * Created by Clément Lelong on 23/05/16.
 */
trait ExplorationDAO extends CommonAttributes {
  def insert(doc: Exploration): Future[WriteResult]
  def findAndModify(selector: BSONDocument, modifier: Exploration, fields: BSONDocument): Future[Option[Exploration]]
  def getList(page: Int, numberPerPage: Int): Future[List[Exploration]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1)): Future[Option[Exploration]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Exploration]]
  def count(selector: JsObject): Future[Int]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}
