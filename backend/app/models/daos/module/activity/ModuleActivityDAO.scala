package models.daos.module.activity

import model.mercure.daos.CommonAttributes
import models.daos.module.activity.ModuleActivity
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

/**
 * Created by Clément Lelong on 23/05/16.
 */
trait ModuleActivityDAO extends CommonAttributes {
  def insert(doc: ModuleActivity): Future[WriteResult]
  def findAndModify(selector: BSONDocument, modifier: ModuleActivity, fields: BSONDocument): Future[Option[ModuleActivity]]
  def getList(page: Int, numberPerPage: Int): Future[List[ModuleActivity]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1)): Future[Option[ModuleActivity]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[ModuleActivity]]
  def count(selector: JsObject): Future[Int]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}
