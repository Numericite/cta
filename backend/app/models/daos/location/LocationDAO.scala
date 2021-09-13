package models.daos.location

import model.mercure.daos.CommonAttributes
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

/**
 * Created by Clément Lelong O8/05/17
 */
trait LocationDAO extends CommonAttributes {

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), page: Int = 1, numberPerPage: Int = 25): Future[List[Location]]
  def insert(doc: Location): Future[WriteResult]
  def findAndModify(selector: BSONDocument, modifier: Location, fields: BSONDocument): Future[Option[Location]]
  def getList(page: Int, numberPerPage: Int): Future[List[Location]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Location]]
  def aggregate(pipeLine: BSONArray): Future[JsObject]

}
