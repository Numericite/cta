package services.association

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.association.{ Association, AssociationDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class AssociationServiceImpl @Inject() (associationDAO: AssociationDAO) extends AssociationService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = associationDAO.update(selector, modifier)

  def insert(doc: Association): Future[WriteResult] = associationDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = associationDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Association, fields: BSONDocument): Future[Option[Association]] = associationDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Association]] = associationDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Association]] = associationDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Association]] = associationDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = associationDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = associationDAO.aggregate(pipeLine)
}
