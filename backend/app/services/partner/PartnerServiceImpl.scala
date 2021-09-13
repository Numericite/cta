package services.partner

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.partner.{ Partner, PartnerDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class PartnerServiceImpl @Inject() (partnerDAO: PartnerDAO) extends PartnerService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = partnerDAO.update(selector, modifier)

  def insert(doc: Partner): Future[WriteResult] = partnerDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = partnerDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Partner, fields: BSONDocument): Future[Option[Partner]] = partnerDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Partner]] = partnerDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Partner]] = partnerDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Partner]] = partnerDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = partnerDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = partnerDAO.aggregate(pipeLine)
}
