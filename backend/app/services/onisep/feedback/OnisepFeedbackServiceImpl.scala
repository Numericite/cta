package services.onisep.feedback

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.onisep.feedback.{ OnisepFeedback, OnisepFeedbackDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class OnisepFeedbackServiceImpl @Inject() (onisepFeedbackDAO: OnisepFeedbackDAO) extends OnisepFeedbackService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = onisepFeedbackDAO.update(selector, modifier)

  def insert(doc: OnisepFeedback): Future[WriteResult] = onisepFeedbackDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = onisepFeedbackDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: OnisepFeedback, fields: BSONDocument): Future[Option[OnisepFeedback]] = onisepFeedbackDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[OnisepFeedback]] = onisepFeedbackDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[OnisepFeedback]] = onisepFeedbackDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[OnisepFeedback]] = onisepFeedbackDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = onisepFeedbackDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = onisepFeedbackDAO.aggregate(pipeLine)
}
