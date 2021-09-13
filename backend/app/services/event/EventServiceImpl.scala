package services.event

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.event.{ Event, EventDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class EventServiceImpl @Inject() (EventDAO: EventDAO) extends EventService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = EventDAO.update(selector, modifier)

  def insert(doc: Event): Future[WriteResult] = EventDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = EventDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Event, fields: BSONDocument): Future[Option[Event]] = EventDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Event]] = EventDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Event]] = EventDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Event]] = EventDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = EventDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = EventDAO.aggregate(pipeLine)
}
