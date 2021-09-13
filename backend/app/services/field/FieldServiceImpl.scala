package services.field

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.field.{ Field, FieldDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class FieldServiceImpl @Inject() (fieldDAO: FieldDAO) extends FieldService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = fieldDAO.update(selector, modifier)

  def insert(doc: Field): Future[WriteResult] = fieldDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = fieldDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Field, fields: BSONDocument): Future[Option[Field]] = fieldDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Field]] = fieldDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Field]] = fieldDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Field]] = fieldDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = fieldDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = fieldDAO.aggregate(pipeLine)
}
