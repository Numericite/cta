package services.activity.log

import com.google.inject.Inject
import models.daos.activity.log.{ ActivityLog, ActivityLogDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.BSONDocument

import scala.concurrent.Future

/**
 * Created by Clément Lelong on 27/05/16.
 */
class ActivityLogServiceImpl @Inject() (activityLogDAO: ActivityLogDAO) extends ActivityLogService {
  def find(selector: BSONDocument, sort: JsObject = Json.obj("created_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[ActivityLog]] = activityLogDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def insert(doc: ActivityLog): Future[WriteResult] = activityLogDAO.insert(doc)
  def findAndModify(selector: BSONDocument, modifier: ActivityLog, fields: BSONDocument): Future[Option[ActivityLog]] = activityLogDAO.findAndModify(selector, modifier, fields)
  def getList(page: Int, numberPerPage: Int): Future[List[ActivityLog]] = activityLogDAO.getList(page, numberPerPage)
  override def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = activityLogDAO.update(selector, modifier)
  override def remove(selector: BSONDocument): Future[WriteResult] = activityLogDAO.remove(selector)
  override def count(selector: JsObject): Future[Int] = activityLogDAO.count(selector)
}

