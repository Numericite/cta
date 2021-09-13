package models.daos.document

import model.mercure.daos.CommonAttributes
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.BSONDocument

import scala.concurrent.Future

/**
 * Created by madalien on 23/05/16.
 */
trait DocumentDAO extends CommonAttributes {
  def insert(doc: Document): Future[WriteResult]
  def findAndModify(selector: BSONDocument, modifier: Document, fields: BSONDocument, upsertIn: Boolean = false): Future[Option[Document]]
  def getList(page: Int, numberPerPage: Int): Future[List[Document]]
  def count(selector: JsObject): Future[Int]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), page: Int = 1, numberPerPage: Int = 1000): Future[List[Document]]
}
