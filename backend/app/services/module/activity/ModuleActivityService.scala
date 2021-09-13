package services.module.activity

/**
 * Created by Clément Lelong on 08/05/17.
 */
import model.mercure.daos.CommonAttributes
import models.daos.module.activity.ModuleActivity
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

trait ModuleActivityService extends CommonAttributes {
  def insert(doc: ModuleActivity): Future[WriteResult]
  def getList(page: Int, numberPerPage: Int): Future[List[ModuleActivity]]
  def findAndModify(selector: BSONDocument, modifier: ModuleActivity, fields: BSONDocument): Future[Option[ModuleActivity]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1)): Future[Option[ModuleActivity]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[ModuleActivity]]
  def count(selector: JsObject = Json.obj()): Future[Int]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}
