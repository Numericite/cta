package services.explorationType

/**
 * Created by Clément Lelong on 08/05/17.
 */
import model.mercure.daos.CommonAttributes
import models.daos.explorationType.ExplorationType
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

trait ExplorationTypeService extends CommonAttributes {
  def insert(doc: ExplorationType): Future[WriteResult]
  def getList(page: Int, numberPerPage: Int): Future[List[ExplorationType]]
  def findAndModify(selector: BSONDocument, modifier: ExplorationType, fields: BSONDocument): Future[Option[ExplorationType]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1)): Future[Option[ExplorationType]]
  def find(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[ExplorationType]]
  def count(selector: JsObject = Json.obj()): Future[Int]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}
