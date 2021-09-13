package services.fieldlog

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.fieldlog.{ FieldLog, FieldLogDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class FieldLogServiceImpl @Inject() (fieldLogDAO: FieldLogDAO) extends FieldLogService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = fieldLogDAO.update(selector, modifier)

  def insert(doc: FieldLog): Future[WriteResult] = fieldLogDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = fieldLogDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: FieldLog, fields: BSONDocument): Future[Option[FieldLog]] = fieldLogDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[FieldLog]] = fieldLogDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[FieldLog]] = fieldLogDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[FieldLog]] = fieldLogDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = fieldLogDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = fieldLogDAO.aggregate(pipeLine)
}
