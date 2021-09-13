package services.activity

import com.google.inject.Inject
import models.daos.activity.{ Activity, ActivityDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.BSONDocument

import scala.concurrent.Future

/**
 * Created by Clément Lelong on 27/05/16.
 */
class ActivityServiceImpl @Inject() (activityDAO: ActivityDAO) extends ActivityService {
  def find(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Activity]] = activityDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def insert(doc: Activity): Future[WriteResult] = activityDAO.insert(doc)
  def findAndModify(selector: BSONDocument, modifier: Activity, fields: BSONDocument): Future[Option[Activity]] = activityDAO.findAndModify(selector, modifier, fields)
  def getList(page: Int, numberPerPage: Int): Future[List[Activity]] = activityDAO.getList(page, numberPerPage)
  override def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = activityDAO.update(selector, modifier)
  override def remove(selector: BSONDocument): Future[WriteResult] = activityDAO.remove(selector)
  override def count(selector: JsObject): Future[Int] = activityDAO.count(selector)
}

