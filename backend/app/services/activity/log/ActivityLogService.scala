package services.activity.log

import model.mercure.daos.CommonAttributes
import models.daos.activity.log.ActivityLog
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.BSONDocument

import scala.concurrent.Future

/**
 * Created by Clément Lelong on 27/05/16.
 */
trait ActivityLogService extends CommonAttributes {
  def find(selector: BSONDocument, sort: JsObject = Json.obj("created_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[ActivityLog]]
  def insert(doc: ActivityLog): Future[WriteResult]
  def findAndModify(selector: BSONDocument, modifier: ActivityLog, fields: BSONDocument): Future[Option[ActivityLog]]
  def getList(page: Int, numberPerPage: Int): Future[List[ActivityLog]]
  def count(selector: JsObject): Future[Int]
}
