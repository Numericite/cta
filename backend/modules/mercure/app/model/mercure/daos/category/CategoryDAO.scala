package model.mercure.daos.category

import model.mercure.daos.CommonAttributes
import play.api.libs.json.{ Json, JsObject }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.BSONDocument
import scala.concurrent.Future

/**
 * Created by madalien on 27/05/16.
 */
trait CategoryDAO extends CommonAttributes {
  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[List[Category]]
  def insert(doc: Category): Future[WriteResult]
  def findAndModify(selector: BSONDocument, modifier: Category, fields: BSONDocument): Future[Option[Category]]
  def getList(page: Int, numberByPage: Int, sort: JsObject = Json.obj("updated_date" -> -1)): Future[List[Category]]
  def count(selector: JsObject): Future[Int]
}
