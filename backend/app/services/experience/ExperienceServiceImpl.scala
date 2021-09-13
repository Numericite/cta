package services.experience

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.experience.{ Experience, ExperienceDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class ExperienceServiceImpl @Inject() (experienceDAO: ExperienceDAO) extends ExperienceService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = experienceDAO.update(selector, modifier)

  def insert(doc: Experience): Future[WriteResult] = experienceDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = experienceDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Experience, fields: BSONDocument): Future[Option[Experience]] = experienceDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> 1)): Future[Option[Experience]] = experienceDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Experience]] = experienceDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> 1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Experience]] = experienceDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = experienceDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = experienceDAO.aggregate(pipeLine)
}
