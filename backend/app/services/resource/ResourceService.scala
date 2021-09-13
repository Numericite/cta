package services.resource

/**
 * Created by Clément Lelong on 08/05/17.
 */
import model.mercure.daos.CommonAttributes
import models.daos.resource.Resource
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

trait ResourceService extends CommonAttributes {
  def insert(doc: Resource): Future[WriteResult]
  def getList(page: Int, numberPerPage: Int): Future[List[Resource]]
  def findAndModify(selector: BSONDocument, modifier: Resource, fields: BSONDocument): Future[Option[Resource]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Resource]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Resource]]
  def count(selector: JsObject = Json.obj()): Future[Int]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}
