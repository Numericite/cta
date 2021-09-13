package services.school.report

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.school.report.{ SchoolReport, SchoolReportDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class SchoolReportServiceImpl @Inject() (schoolReportDAO: SchoolReportDAO) extends SchoolReportService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = schoolReportDAO.update(selector, modifier)

  def insert(doc: SchoolReport): Future[WriteResult] = schoolReportDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = schoolReportDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: SchoolReport, fields: BSONDocument): Future[Option[SchoolReport]] = schoolReportDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[SchoolReport]] = schoolReportDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[SchoolReport]] = schoolReportDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[SchoolReport]] = schoolReportDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = schoolReportDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = schoolReportDAO.aggregate(pipeLine)
}
