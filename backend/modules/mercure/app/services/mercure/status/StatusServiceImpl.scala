package services.mercure.status

import com.google.inject.Inject
import model.mercure.daos.status.{ Status, StatusDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.DB
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.BSONDocument

import scala.concurrent.Future

/**
 * Created by madalien on 25/05/16.
 */
class StatusServiceImpl @Inject() (statusDAO: StatusDAO) extends StatusService {

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[List[Status]] = statusDAO.find(selector, sort)
  def insert(doc: Status): Future[WriteResult] = statusDAO.insert(doc)
  def findAndModify(selector: BSONDocument, modifier: Status, fields: BSONDocument): Future[Option[Status]] = statusDAO.findAndModify(selector, modifier, fields)
  def getList(page: Int, numberByPage: Int): Future[List[Status]] = statusDAO.getList(page, numberByPage)
  override def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = statusDAO.update(selector, modifier)
  override def remove(selector: BSONDocument): Future[WriteResult] = statusDAO.remove(selector)
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Status]] = statusDAO.findOne(selector, sort)
  override def count(selector: JsObject): Future[Int] = statusDAO.count(selector)
}
