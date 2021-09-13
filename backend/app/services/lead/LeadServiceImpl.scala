package services.lead

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.lead.{ Lead, LeadDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class LeadServiceImpl @Inject() (leadDAO: LeadDAO) extends LeadService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = leadDAO.update(selector, modifier)

  def insert(doc: Lead): Future[WriteResult] = leadDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = leadDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Lead, fields: BSONDocument): Future[Option[Lead]] = leadDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Lead]] = leadDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Lead]] = leadDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Lead]] = leadDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = leadDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = leadDAO.aggregate(pipeLine)
}
