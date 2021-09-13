package services.domain

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.domain.{ Domain, DomainDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class DomainServiceImpl @Inject() (domainDAO: DomainDAO) extends DomainService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = domainDAO.update(selector, modifier)

  def insert(doc: Domain): Future[WriteResult] = domainDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = domainDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Domain, fields: BSONDocument): Future[Option[Domain]] = domainDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Domain]] = domainDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Domain]] = domainDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Domain]] = domainDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = domainDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = domainDAO.aggregate(pipeLine)
}
