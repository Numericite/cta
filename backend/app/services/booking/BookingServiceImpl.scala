package services.booking

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.booking.{ Booking, BookingDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class BookingServiceImpl @Inject() (BookingDAO: BookingDAO) extends BookingService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = BookingDAO.update(selector, modifier)

  def insert(doc: Booking): Future[WriteResult] = BookingDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = BookingDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Booking, fields: BSONDocument): Future[Option[Booking]] = BookingDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Booking]] = BookingDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Booking]] = BookingDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Booking]] = BookingDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = BookingDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = BookingDAO.aggregate(pipeLine)
}
