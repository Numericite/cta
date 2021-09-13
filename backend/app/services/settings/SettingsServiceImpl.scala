package services.settings

/**
 * Created by madalien on 12/19/16.
 */

import com.google.inject.Inject
import models.daos.settings.Settings
import models.daos.settings.SettingsDAO
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class SettingsServiceImpl @Inject() (settingsDAO: SettingsDAO) extends SettingsService {
  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), page: Int = 1, numberByPage: Int = 25): Future[List[Settings]] = settingsDAO.find(selector, sort, page, numberByPage)
  def insert(doc: Settings): Future[WriteResult] = settingsDAO.insert(doc)
  def findAndModify(selector: BSONDocument, modifier: Settings, fields: BSONDocument): Future[Option[Settings]] = settingsDAO.findAndModify(selector, modifier, fields)
  def getList(page: Int, numberByPage: Int): Future[List[Settings]] = settingsDAO.getList(page, numberByPage)
  override def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = settingsDAO.update(selector, modifier)
  override def remove(selector: BSONDocument): Future[WriteResult] = settingsDAO.remove(selector)
  def count(selector: BSONDocument): Future[Int] = settingsDAO.count(selector)
  def distinct(distinctKey: String, selector: JsObject) = settingsDAO.distinct(distinctKey, selector)
  def aggregate(pipeLine: BSONArray): Future[JsObject] = settingsDAO.aggregate(pipeLine)
}
