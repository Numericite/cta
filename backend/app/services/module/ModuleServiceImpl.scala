package services.module

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.module.{ Module, ModuleDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class ModuleServiceImpl @Inject() (moduleDAO: ModuleDAO) extends ModuleService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = moduleDAO.update(selector, modifier)

  def insert(doc: Module): Future[WriteResult] = moduleDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = moduleDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Module, fields: BSONDocument): Future[Option[Module]] = moduleDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1)): Future[Option[Module]] = moduleDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Module]] = moduleDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Module]] = moduleDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = moduleDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = moduleDAO.aggregate(pipeLine)
}
