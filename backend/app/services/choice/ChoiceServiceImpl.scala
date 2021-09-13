package services.choice

/**
 * Created by Clément Lelong on 08/05/17.
 */
import com.google.inject.Inject
import models.daos.choice.{ Choice, ChoiceDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }

import scala.concurrent.Future

class ChoiceServiceImpl @Inject() (choiceDAO: ChoiceDAO) extends ChoiceService {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = choiceDAO.update(selector, modifier)

  def insert(doc: Choice): Future[WriteResult] = choiceDAO.insert(doc)

  def remove(selector: BSONDocument): Future[WriteResult] = choiceDAO.remove(selector)

  def findAndModify(selector: BSONDocument, modifier: Choice, fields: BSONDocument): Future[Option[Choice]] = choiceDAO.findAndModify(selector, modifier, fields)

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Choice]] = choiceDAO.findOne(selector, sort)
  def getList(page: Int, numberPerPage: Int): Future[List[Choice]] = choiceDAO.getList(page, numberPerPage)

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Choice]] = choiceDAO.find(selector, sort, projectionIn = null, page, numberPerPage)
  def count(selector: JsObject = Json.obj()): Future[Int] = choiceDAO.count(selector)

  def aggregate(pipeLine: BSONArray): Future[JsObject] = choiceDAO.aggregate(pipeLine)
}
