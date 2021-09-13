package models.daos.tag

/**
 * Created by Clément Lelong on 24/09/16.
 */

import model.mercure.daos.CommonAttributes
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

trait TagDAO extends CommonAttributes {
  def insert(doc: Tag): Future[WriteResult]
  def findAndModify(selector: BSONDocument, modifier: Tag, fields: BSONDocument): Future[Option[Tag]]
  def getList(page: Int, numberPerPage: Int): Future[List[Tag]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Tag]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Tag]]
  def count(selector: JsObject): Future[Int]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}
