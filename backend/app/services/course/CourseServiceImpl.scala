package services.course

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.course.{ Course, CourseDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class CourseServiceImpl @Inject() (courseDAO: CourseDAO) extends CourseService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = courseDAO.update(selector, modifier)

  def insert(doc: Course): Future[WriteResult] = courseDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = courseDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Course, fields: BSONDocument): Future[Option[Course]] = courseDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Course]] = courseDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Course]] = courseDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Course]] = courseDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = courseDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = courseDAO.aggregate(pipeLine)
}
