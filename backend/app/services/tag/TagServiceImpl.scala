package services.tag

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.tag.{ Tag, TagDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class TagServiceImpl @Inject() (tagDAO: TagDAO) extends TagService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = tagDAO.update(selector, modifier)

  def insert(doc: Tag): Future[WriteResult] = tagDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = tagDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Tag, fields: BSONDocument): Future[Option[Tag]] = tagDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Tag]] = tagDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Tag]] = tagDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Tag]] = tagDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = tagDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = tagDAO.aggregate(pipeLine)
}
