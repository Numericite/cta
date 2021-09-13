package services.version

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.version.{ Version, VersionDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class VersionServiceImpl @Inject() (versionDAO: VersionDAO) extends VersionService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = versionDAO.update(selector, modifier)

  def insert(doc: Version): Future[WriteResult] = versionDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = versionDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Version, fields: BSONDocument): Future[Option[Version]] = versionDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Version]] = versionDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Version]] = versionDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Version]] = versionDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = versionDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = versionDAO.aggregate(pipeLine)
}
