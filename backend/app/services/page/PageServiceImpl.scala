package services.page

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.page.{ Page, PageDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class PageServiceImpl @Inject() (pageDAO: PageDAO) extends PageService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = pageDAO.update(selector, modifier)

  def insert(doc: Page): Future[WriteResult] = pageDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = pageDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Page, fields: BSONDocument): Future[Option[Page]] = pageDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Page]] = pageDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Page]] = pageDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Page]] = pageDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = pageDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = pageDAO.aggregate(pipeLine)
}
