package services.location

import com.google.inject.Inject
import models.daos.location.{ Location, LocationDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

/**
 * Created by Clément Lelong O8/05/17
 */

class LocationServiceImpl @Inject() (locationDAO: LocationDAO) extends LocationService {
  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), page: Int = 1, numberPerPage: Int = 25): Future[List[Location]] = locationDAO.find(selector, sort, page, numberPerPage)
  def insert(doc: Location): Future[WriteResult] = locationDAO.insert(doc)
  def findAndModify(selector: BSONDocument, modifier: Location, fields: BSONDocument): Future[Option[Location]] = locationDAO.findAndModify(selector, modifier, fields)
  def getList(page: Int, numberPerPage: Int): Future[List[Location]] = locationDAO.getList(page, numberPerPage)
  override def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = locationDAO.update(selector, modifier)
  override def remove(selector: BSONDocument): Future[WriteResult] = locationDAO.remove(selector)
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Location]] = locationDAO.findOne(selector, sort)
  def aggregate(pipeLine: BSONArray): Future[JsObject] = locationDAO.aggregate(pipeLine)
}