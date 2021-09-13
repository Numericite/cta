package services.onisep.feedback

/**
 * Created by Clément Lelong on 08/05/17.
 */
import model.mercure.daos.CommonAttributes
import models.daos.onisep.feedback.OnisepFeedback
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

trait OnisepFeedbackService extends CommonAttributes {
  def insert(doc: OnisepFeedback): Future[WriteResult]
  def getList(page: Int, numberPerPage: Int): Future[List[OnisepFeedback]]
  def findAndModify(selector: BSONDocument, modifier: OnisepFeedback, fields: BSONDocument): Future[Option[OnisepFeedback]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[OnisepFeedback]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[OnisepFeedback]]
  def count(selector: JsObject = Json.obj()): Future[Int]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}
