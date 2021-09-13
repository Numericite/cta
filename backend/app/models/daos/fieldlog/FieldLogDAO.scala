package models.daos.fieldlog

import model.mercure.daos.CommonAttributes
import models.daos.fieldlog.FieldLog
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

/**
 * Created by Clément Lelong on 23/05/16.
 */
trait FieldLogDAO extends CommonAttributes {
  def insert(doc: FieldLog): Future[WriteResult]
  def findAndModify(selector: BSONDocument, modifier: FieldLog, fields: BSONDocument): Future[Option[FieldLog]]
  def getList(page: Int, numberPerPage: Int): Future[List[FieldLog]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1)): Future[Option[FieldLog]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[FieldLog]]
  def count(selector: JsObject): Future[Int]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}
