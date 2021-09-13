package services.faq

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.faq.{ Faq, FaqDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class FaqServiceImpl @Inject() (faqDAO: FaqDAO) extends FaqService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = faqDAO.update(selector, modifier)

  def insert(doc: Faq): Future[WriteResult] = faqDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = faqDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Faq, fields: BSONDocument): Future[Option[Faq]] = faqDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Faq]] = faqDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Faq]] = faqDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Faq]] = faqDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = faqDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = faqDAO.aggregate(pipeLine)
}
