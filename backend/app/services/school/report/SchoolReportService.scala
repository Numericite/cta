package services.school.report

/**
 * Created by Clément Lelong on 08/05/17.
 */
import model.mercure.daos.CommonAttributes
import models.daos.school.report.SchoolReport
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

trait SchoolReportService extends CommonAttributes {
  def insert(doc: SchoolReport): Future[WriteResult]
  def getList(page: Int, numberPerPage: Int): Future[List[SchoolReport]]
  def findAndModify(selector: BSONDocument, modifier: SchoolReport, fields: BSONDocument): Future[Option[SchoolReport]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[SchoolReport]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[SchoolReport]]
  def count(selector: JsObject = Json.obj()): Future[Int]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}
