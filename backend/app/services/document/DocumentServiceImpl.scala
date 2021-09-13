package services.document

import com.google.inject.Inject
import models.daos.document.{ Document, DocumentDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.BSONDocument

import scala.concurrent.Future

/**
 * Created by madalien on 25/05/16.
 */

class DocumentServiceImpl @Inject() (val documentDAO: DocumentDAO) extends DocumentService {

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), page: Int, numberPerPage: Int): Future[List[Document]] = documentDAO.find(selector, sort, page, numberPerPage)
  def insert(doc: Document): Future[WriteResult] = documentDAO.insert(doc)
  def findAndModify(selector: BSONDocument, modifier: Document, fields: BSONDocument, upsertIn: Boolean = false): Future[Option[Document]] = documentDAO.findAndModify(selector, modifier, fields, upsertIn)
  def getList(page: Int, numberPerPage: Int): Future[List[Document]] = documentDAO.getList(page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = documentDAO.count(selector)
  override def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = documentDAO.update(selector, modifier)
  override def remove(selector: BSONDocument): Future[WriteResult] = documentDAO.remove(selector)
}
