package models.daos.school.classroom

import model.mercure.daos.CommonAttributes
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

/**
 * Created by Clément Lelong on 23/05/16.
 */
trait SchoolClassroomDAO extends CommonAttributes {
  def insert(doc: SchoolClassroom): Future[WriteResult]
  def findAndModify(selector: BSONDocument, modifier: SchoolClassroom, fields: BSONDocument): Future[Option[SchoolClassroom]]
  def getList(page: Int, numberPerPage: Int): Future[List[SchoolClassroom]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[SchoolClassroom]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[SchoolClassroom]]
  def count(selector: JsObject): Future[Int]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}
