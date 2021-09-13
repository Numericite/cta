package services.notification

/**
 * Created by Clément Lelong O8/05/17
 */
import com.google.inject.Inject
import models.daos.notification.{ Notification, NotificationDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class NotificationServiceImpl @Inject() (notificationDAO: NotificationDAO) extends NotificationService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = notificationDAO.update(selector, modifier)

  def insert(doc: Notification): Future[WriteResult] = notificationDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = notificationDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Notification, fields: BSONDocument): Future[Option[Notification]] = notificationDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Notification]] = notificationDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Notification]] = notificationDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Notification]] = notificationDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = notificationDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = notificationDAO.aggregate(pipeLine)
}
