package services.loginLog

import model.mercure.daos.CommonAttributes
import models.daos.loginLog.LoginLog
import models.daos.location.Location
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

/**
 * Created by Clément Lelong O8/05/17
 */
trait LoginLogService extends CommonAttributes {
  def insert(doc: LoginLog): Future[WriteResult]
  def getList(page: Int, numberPerPage: Int): Future[List[LoginLog]]
  def findAndModify(selector: BSONDocument, modifier: LoginLog, fields: BSONDocument): Future[Option[LoginLog]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("created_date" -> -1)): Future[Option[LoginLog]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("created_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[LoginLog]]
  def count(selector: JsObject = Json.obj()): Future[Int]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}