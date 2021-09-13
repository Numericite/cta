package services.detail

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.detail.{ Detail, DetailDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class DetailServiceImpl @Inject() (detailDAO: DetailDAO) extends DetailService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = detailDAO.update(selector, modifier)

  def insert(doc: Detail): Future[WriteResult] = detailDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = detailDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Detail, fields: BSONDocument): Future[Option[Detail]] = detailDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Detail]] = detailDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Detail]] = detailDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Detail]] = detailDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = detailDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = detailDAO.aggregate(pipeLine)
}
