package services.result

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.result.{ Result, ResultDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ MultiBulkWriteResult, UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class ResultServiceImpl @Inject() (resultDAO: ResultDAO) extends ResultService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = resultDAO.update(selector, modifier)

  def insert(doc: Result): Future[WriteResult] = resultDAO.insert(doc)
  def insertMany(docs: List[Result]): Future[MultiBulkWriteResult] = resultDAO.insertMany(docs)

  def remove(selector: BSONDocument): Future[WriteResult] = resultDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Result, fields: BSONDocument): Future[Option[Result]] = resultDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Result]] = resultDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Result]] = resultDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Result]] = resultDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = resultDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = resultDAO.aggregate(pipeLine)
}
