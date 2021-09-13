package model.mercure.daos.email

import model.mercure.daos.CommonAttributes
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.BSONDocument

import scala.concurrent.Future

/**
 * Created by madalien on 23/05/16.
 */
trait TemplateDAO extends CommonAttributes {

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[List[Template]]
  def insert(doc: Template): Future[WriteResult]
  def findAndModify(selector: BSONDocument, modifier: Template, fields: BSONDocument): Future[Option[Template]]
  def getList(page: Int, numberByPage: Int): Future[List[Template]]
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Template]]
  def count(selector: JsObject): Future[Int]

}
