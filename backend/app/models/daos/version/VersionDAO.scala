package models.daos.version

import model.mercure.daos.CommonAttributes
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

/**
 * Created by Clément Lelong on 23/05/16.
 */
trait VersionDAO extends CommonAttributes {
  def insert(doc: Version): Future[WriteResult]
  def findAndModify(selector: BSONDocument, modifier: Version, fields: BSONDocument): Future[Option[Version]]
  def getList(page: Int, numberPerPage: Int): Future[List[Version]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Version]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Version]]
  def count(selector: JsObject): Future[Int]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}
