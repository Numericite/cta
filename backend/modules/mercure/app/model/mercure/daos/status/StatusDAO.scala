package model.mercure.daos.status

import model.mercure.daos.CommonAttributes
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.BSONDocument

import scala.concurrent.Future

/**
 * Created by madalien on 23/05/16.
 */
trait StatusDAO extends CommonAttributes {

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[List[Status]]
  def insert(doc: Status): Future[WriteResult]
  def findAndModify(selector: BSONDocument, modifier: Status, fields: BSONDocument): Future[Option[Status]]
  def getList(page: Int, numberByPage: Int): Future[List[Status]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Status]]
  def count(selector: JsObject): Future[Int]

}
