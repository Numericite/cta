package services.school.classroom

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.school.classroom.{ SchoolClassroom, SchoolClassroomDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class SchoolClassroomServiceImpl @Inject() (schoolClassroomDAO: SchoolClassroomDAO) extends SchoolClassroomService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = schoolClassroomDAO.update(selector, modifier)

  def insert(doc: SchoolClassroom): Future[WriteResult] = schoolClassroomDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = schoolClassroomDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: SchoolClassroom, fields: BSONDocument): Future[Option[SchoolClassroom]] = schoolClassroomDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[SchoolClassroom]] = schoolClassroomDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[SchoolClassroom]] = schoolClassroomDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[SchoolClassroom]] = schoolClassroomDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = schoolClassroomDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = schoolClassroomDAO.aggregate(pipeLine)
}
