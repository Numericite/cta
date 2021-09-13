package services.explorationType

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.explorationType.{ ExplorationType, ExplorationTypeDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class ExplorationTypeServiceImpl @Inject() (explorationTypeDAO: ExplorationTypeDAO) extends ExplorationTypeService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = explorationTypeDAO.update(selector, modifier)

  def insert(doc: ExplorationType): Future[WriteResult] = explorationTypeDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = explorationTypeDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: ExplorationType, fields: BSONDocument): Future[Option[ExplorationType]] = explorationTypeDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1)): Future[Option[ExplorationType]] = explorationTypeDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[ExplorationType]] = explorationTypeDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[ExplorationType]] = explorationTypeDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = explorationTypeDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = explorationTypeDAO.aggregate(pipeLine)
}
