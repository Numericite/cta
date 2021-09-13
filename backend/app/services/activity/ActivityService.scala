package services.activity

import model.mercure.daos.CommonAttributes
import models.daos.activity.Activity
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.BSONDocument

import scala.concurrent.Future

/**
 * Created by Clément Lelong on 27/05/16.
 */
trait ActivityService extends CommonAttributes {
  def find(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Activity]]
  def insert(doc: Activity): Future[WriteResult]
  def findAndModify(selector: BSONDocument, modifier: Activity, fields: BSONDocument): Future[Option[Activity]]
  def getList(page: Int, numberPerPage: Int): Future[List[Activity]]
  def count(selector: JsObject): Future[Int]
}
