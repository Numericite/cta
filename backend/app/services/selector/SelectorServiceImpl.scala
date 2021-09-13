package services.selector

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.selector.{ Selector, SelectorDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class SelectorServiceImpl @Inject() (selectorDAO: SelectorDAO) extends SelectorService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = selectorDAO.update(selector, modifier)

  def insert(doc: Selector): Future[WriteResult] = selectorDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = selectorDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Selector, fields: BSONDocument): Future[Option[Selector]] = selectorDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Selector]] = selectorDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Selector]] = selectorDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Selector]] = selectorDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = selectorDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = selectorDAO.aggregate(pipeLine)
}
