package services.file

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.file.{ File, FileDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class FileServiceImpl @Inject() (fileDAO: FileDAO) extends FileService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = fileDAO.update(selector, modifier)

  def insert(doc: File): Future[WriteResult] = fileDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = fileDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: File, fields: BSONDocument): Future[Option[File]] = fileDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[File]] = fileDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[File]] = fileDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[File]] = fileDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = fileDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = fileDAO.aggregate(pipeLine)
}
