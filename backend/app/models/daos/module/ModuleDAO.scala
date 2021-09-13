package models.daos.module

import model.mercure.daos.CommonAttributes
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

/**
 * Created by Clément Lelong on 23/05/16.
 */
trait ModuleDAO extends CommonAttributes {
  def insert(doc: Module): Future[WriteResult]
  def findAndModify(selector: BSONDocument, modifier: Module, fields: BSONDocument): Future[Option[Module]]
  def getList(page: Int, numberPerPage: Int): Future[List[Module]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> 1)): Future[Option[Module]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Module]]
  def count(selector: JsObject): Future[Int]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}
