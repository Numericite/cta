package services.post

import com.google.inject.Inject
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.BSONDocument
import models.daos.post.{ Post, PostDAO }

import scala.concurrent.Future

/**
 * Created by Clément Lelong on 27/05/16.
 */
class PostServiceImpl @Inject() (postDAO: PostDAO) extends PostService {
  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Post]] = postDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def insert(doc: Post): Future[WriteResult] = postDAO.insert(doc)
  def findAndModify(selector: BSONDocument, modifier: Post, fields: BSONDocument): Future[Option[Post]] = postDAO.findAndModify(selector, modifier, fields)
  def getList(page: Int, numberPerPage: Int): Future[List[Post]] = postDAO.getList(page, numberPerPage)
  override def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = postDAO.update(selector, modifier)
  override def remove(selector: BSONDocument): Future[WriteResult] = postDAO.remove(selector)
  override def count(selector: JsObject): Future[Int] = postDAO.count(selector)
}

