package services.selection

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.selection.{ Selection, SelectionDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class SelectionServiceImpl @Inject() (selectionDAO: SelectionDAO) extends SelectionService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = selectionDAO.update(selector, modifier)

  def insert(doc: Selection): Future[WriteResult] = selectionDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = selectionDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Selection, fields: BSONDocument): Future[Option[Selection]] = selectionDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Selection]] = selectionDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Selection]] = selectionDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Selection]] = selectionDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = selectionDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = selectionDAO.aggregate(pipeLine)
}
