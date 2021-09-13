package services.mercure.category

/**
 * Created by madalien on 27/05/16.
 */

import model.mercure.daos.CommonAttributes
import model.mercure.daos.category.Category
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.BSONDocument

import scala.concurrent.Future

trait CategoryService extends CommonAttributes {
  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[List[Category]]
  def insert(doc: Category): Future[WriteResult]
  def findAndModify(selector: BSONDocument, modifier: Category, fields: BSONDocument): Future[Option[Category]]
  def getList(page: Int, numberByPage: Int, sort: JsObject = Json.obj("updated_date" -> -1)): Future[List[Category]]
  def count(selector: JsObject): Future[Int]
}
