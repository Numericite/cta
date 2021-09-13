package services.meeting

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.meeting.{ Meeting, MeetingDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class MeetingServiceImpl @Inject() (meetingDAO: MeetingDAO) extends MeetingService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = meetingDAO.update(selector, modifier)

  def insert(doc: Meeting): Future[WriteResult] = meetingDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = meetingDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Meeting, fields: BSONDocument): Future[Option[Meeting]] = meetingDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Meeting]] = meetingDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Meeting]] = meetingDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Meeting]] = meetingDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = meetingDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = meetingDAO.aggregate(pipeLine)
}
