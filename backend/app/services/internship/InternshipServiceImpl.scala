package services.internship

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.internship.{ Internship, InternshipDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class InternshipServiceImpl @Inject() (internshipDAO: InternshipDAO) extends InternshipService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = internshipDAO.update(selector, modifier)

  def insert(doc: Internship): Future[WriteResult] = internshipDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = internshipDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Internship, fields: BSONDocument): Future[Option[Internship]] = internshipDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Internship]] = internshipDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Internship]] = internshipDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Internship]] = internshipDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = internshipDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = internshipDAO.aggregate(pipeLine)
}
