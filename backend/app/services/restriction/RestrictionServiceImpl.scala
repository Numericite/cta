package services.restriction

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.restriction.{ Restriction, RestrictionDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class RestrictionServiceImpl @Inject() (restrictionDAO: RestrictionDAO) extends RestrictionService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = restrictionDAO.update(selector, modifier)

  def insert(doc: Restriction): Future[WriteResult] = restrictionDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = restrictionDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Restriction, fields: BSONDocument): Future[Option[Restriction]] = restrictionDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1)): Future[Option[Restriction]] = restrictionDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Restriction]] = restrictionDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Restriction]] = restrictionDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = restrictionDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = restrictionDAO.aggregate(pipeLine)
}
