package services.settings

/**
 * Created by madalien on 12/19/16.
 */
import model.mercure.daos.CommonAttributes
import models.daos.settings.Settings
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

trait SettingsService extends CommonAttributes {
  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), page: Int = 1, numberByPage: Int = 25): Future[List[Settings]]
  def insert(doc: Settings): Future[WriteResult]
  def findAndModify(selector: BSONDocument, modifier: Settings, fields: BSONDocument): Future[Option[Settings]]
  def getList(page: Int, numberByPage: Int): Future[List[Settings]]
  def count(selector: BSONDocument): Future[Int]
  def distinct(distinctKey: String, selector: JsObject): Future[List[String]]
  def aggregate(pipeLine: BSONArray): Future[JsObject]
}

