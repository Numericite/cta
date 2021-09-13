package services.selector

/**
 * Created by Clément Lelong on 08/05/17.
 */
import model.mercure.daos.CommonAttributes
import models.daos.selector.Selector
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

trait SelectorService extends CommonAttributes {
  def insert(doc: Selector): Future[WriteResult]
  def getList(page: Int, numberPerPage: Int): Future[List[Selector]]
  def findAndModify(selector: BSONDocument, modifier: Selector, fields: BSONDocument): Future[Option[Selector]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Selector]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Selector]]
  def count(selector: JsObject = Json.obj()): Future[Int]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}
