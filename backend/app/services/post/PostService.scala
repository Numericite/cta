package services.post

import model.mercure.daos.CommonAttributes
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.BSONDocument
import models.daos.post.Post

import scala.concurrent.Future

/**
 * Created by Clément Lelong on 27/05/16.
 */
trait PostService extends CommonAttributes {
  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Post]]
  def insert(doc: Post): Future[WriteResult]
  def findAndModify(selector: BSONDocument, modifier: Post, fields: BSONDocument): Future[Option[Post]]
  def getList(page: Int, numberPerPage: Int): Future[List[Post]]
  def count(selector: JsObject): Future[Int]
}
