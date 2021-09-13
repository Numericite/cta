package models.daos.school.report

import model.mercure.daos.CommonAttributes
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

/**
 * Created by Clément Lelong on 23/05/16.
 */
trait SchoolReportDAO extends CommonAttributes {
  def insert(doc: SchoolReport): Future[WriteResult]
  def findAndModify(selector: BSONDocument, modifier: SchoolReport, fields: BSONDocument): Future[Option[SchoolReport]]
  def getList(page: Int, numberPerPage: Int): Future[List[SchoolReport]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[SchoolReport]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[SchoolReport]]
  def count(selector: JsObject): Future[Int]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}
