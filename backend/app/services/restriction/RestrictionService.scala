package services.restriction

/**
 * Created by Clément Lelong on 08/05/17.
 */
import model.mercure.daos.CommonAttributes
import models.daos.restriction.Restriction
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

trait RestrictionService extends CommonAttributes {
  def insert(doc: Restriction): Future[WriteResult]
  def getList(page: Int, numberPerPage: Int): Future[List[Restriction]]
  def findAndModify(selector: BSONDocument, modifier: Restriction, fields: BSONDocument): Future[Option[Restriction]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1)): Future[Option[Restriction]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Restriction]]
  def count(selector: JsObject = Json.obj()): Future[Int]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}
