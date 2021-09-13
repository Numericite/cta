package services.company

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.company.{ Company, CompanyDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class CompanyServiceImpl @Inject() (companyDAO: CompanyDAO) extends CompanyService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = companyDAO.update(selector, modifier)

  def insert(doc: Company): Future[WriteResult] = companyDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = companyDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Company, fields: BSONDocument): Future[Option[Company]] = companyDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Company]] = companyDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Company]] = companyDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Company]] = companyDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = companyDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = companyDAO.aggregate(pipeLine)
}
