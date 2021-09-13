package services.exploration

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.exploration.{ Exploration, ExplorationDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class ExplorationServiceImpl @Inject() (explorationDAO: ExplorationDAO) extends ExplorationService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = explorationDAO.update(selector, modifier)

  def insert(doc: Exploration): Future[WriteResult] = explorationDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = explorationDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Exploration, fields: BSONDocument): Future[Option[Exploration]] = explorationDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Exploration]] = explorationDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Exploration]] = explorationDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Exploration]] = explorationDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = explorationDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = explorationDAO.aggregate(pipeLine)
}
