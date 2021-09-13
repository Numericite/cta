package services.loginLog

import com.google.inject.Inject
import models.daos.loginLog.{ LoginLog, LoginLogDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

/**
 * Created by Clément Lelong O8/05/17
 */

class LoginLogServiceImpl @Inject() (loginLogDAO: LoginLogDAO) extends LoginLogService {
  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = loginLogDAO.update(selector, modifier)

  def insert(doc: LoginLog): Future[WriteResult] = loginLogDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = loginLogDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: LoginLog, fields: BSONDocument): Future[Option[LoginLog]] = loginLogDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("created_date" -> -1)): Future[Option[LoginLog]] = loginLogDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[LoginLog]] = loginLogDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("created_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[LoginLog]] = loginLogDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = loginLogDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = loginLogDAO.aggregate(pipeLine)
}