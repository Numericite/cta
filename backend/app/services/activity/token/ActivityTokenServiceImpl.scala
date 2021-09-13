package services.activity.token

import com.google.inject.Inject
import models.daos.activity.token.{ ActivityToken, ActivityTokenDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.BSONDocument

import scala.concurrent.Future

/**
 * Created by Clément Lelong on 27/05/16.
 */
class ActivityTokenServiceImpl @Inject() (activityTokenDAO: ActivityTokenDAO) extends ActivityTokenService {
  def find(selector: BSONDocument, sort: JsObject = Json.obj("created_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[ActivityToken]] = activityTokenDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def insert(doc: ActivityToken): Future[WriteResult] = activityTokenDAO.insert(doc)
  def findAndModify(selector: BSONDocument, modifier: ActivityToken, fields: BSONDocument): Future[Option[ActivityToken]] = activityTokenDAO.findAndModify(selector, modifier, fields)
  def getList(page: Int, numberPerPage: Int): Future[List[ActivityToken]] = activityTokenDAO.getList(page, numberPerPage)
  override def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = activityTokenDAO.update(selector, modifier)
  override def remove(selector: BSONDocument): Future[WriteResult] = activityTokenDAO.remove(selector)
  override def count(selector: JsObject): Future[Int] = activityTokenDAO.count(selector)
}

