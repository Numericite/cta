package services.document

import model.mercure.daos.CommonAttributes
import models.daos.document.Document
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.BSONDocument

import scala.concurrent.Future

/**
 * Created by madalien on 25/05/16.
 */

trait DocumentService extends CommonAttributes {

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), page: Int, numberPerPage: Int): Future[List[Document]]
  def insert(doc: Document): Future[WriteResult]
  def findAndModify(selector: BSONDocument, modifier: Document, fields: BSONDocument, upsertIn: Boolean = false): Future[Option[Document]]
  def count(selector: JsObject = Json.obj()): Future[Int]
  def getList(page: Int, numberPerPage: Int): Future[List[Document]]

}
