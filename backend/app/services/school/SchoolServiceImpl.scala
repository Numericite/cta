package services.school

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.school.{ School, SchoolDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class SchoolServiceImpl @Inject() (schoolDAO: SchoolDAO) extends SchoolService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = schoolDAO.update(selector, modifier)

  def insert(doc: School): Future[WriteResult] = schoolDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = schoolDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: School, fields: BSONDocument): Future[Option[School]] = schoolDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[School]] = schoolDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[School]] = schoolDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[School]] = schoolDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = schoolDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = schoolDAO.aggregate(pipeLine)
}
