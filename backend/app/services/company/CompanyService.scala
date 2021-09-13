package services.company

/**
 * Created by Clément Lelong on 08/05/17.
 */
import model.mercure.daos.CommonAttributes
import models.daos.company.Company
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

trait CompanyService extends CommonAttributes {
  def insert(doc: Company): Future[WriteResult]
  def getList(page: Int, numberPerPage: Int): Future[List[Company]]
  def findAndModify(selector: BSONDocument, modifier: Company, fields: BSONDocument): Future[Option[Company]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Company]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Company]]
  def count(selector: JsObject = Json.obj()): Future[Int]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}
