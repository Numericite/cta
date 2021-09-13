package services.activity.token

import model.mercure.daos.CommonAttributes
import models.daos.activity.token.ActivityToken
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.BSONDocument

import scala.concurrent.Future

/**
 * Created by Clément Lelong on 27/05/16.
 */
trait ActivityTokenService extends CommonAttributes {
  def find(selector: BSONDocument, sort: JsObject = Json.obj("created_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[ActivityToken]]
  def insert(doc: ActivityToken): Future[WriteResult]
  def findAndModify(selector: BSONDocument, modifier: ActivityToken, fields: BSONDocument): Future[Option[ActivityToken]]
  def getList(page: Int, numberPerPage: Int): Future[List[ActivityToken]]
  def count(selector: JsObject): Future[Int]
}
