package services.module.activity

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.module.activity.{ ModuleActivity, ModuleActivityDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class ModuleActivityServiceImpl @Inject() (moduleActivityDAO: ModuleActivityDAO) extends ModuleActivityService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = moduleActivityDAO.update(selector, modifier)

  def insert(doc: ModuleActivity): Future[WriteResult] = moduleActivityDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = moduleActivityDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: ModuleActivity, fields: BSONDocument): Future[Option[ModuleActivity]] = moduleActivityDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1)): Future[Option[ModuleActivity]] = moduleActivityDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[ModuleActivity]] = moduleActivityDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[ModuleActivity]] = moduleActivityDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = moduleActivityDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = moduleActivityDAO.aggregate(pipeLine)
}
