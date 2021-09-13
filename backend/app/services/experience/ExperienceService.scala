package services.experience

/**
 * Created by Clément Lelong on 08/05/17.
 */
import model.mercure.daos.CommonAttributes
import models.daos.experience.Experience
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

trait ExperienceService extends CommonAttributes {
  def insert(doc: Experience): Future[WriteResult]
  def getList(page: Int, numberPerPage: Int): Future[List[Experience]]
  def findAndModify(selector: BSONDocument, modifier: Experience, fields: BSONDocument): Future[Option[Experience]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> 1)): Future[Option[Experience]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> 1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Experience]]
  def count(selector: JsObject = Json.obj()): Future[Int]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}
