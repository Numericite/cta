package services.resource

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.resource.{ Resource, ResourceDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class ResourceServiceImpl @Inject() (resourceDAO: ResourceDAO) extends ResourceService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = resourceDAO.update(selector, modifier)

  def insert(doc: Resource): Future[WriteResult] = resourceDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = resourceDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Resource, fields: BSONDocument): Future[Option[Resource]] = resourceDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Resource]] = resourceDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Resource]] = resourceDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Resource]] = resourceDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = resourceDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = resourceDAO.aggregate(pipeLine)
}
