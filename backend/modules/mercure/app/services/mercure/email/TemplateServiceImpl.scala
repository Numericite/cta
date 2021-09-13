package services.mercure.email

import com.google.inject.Inject
import model.mercure.daos.email.{ Template, TemplateDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.BSONDocument

import scala.concurrent.Future

/**
 * Created by madalien on 25/05/16.
 */
class TemplateServiceImpl @Inject() (templateDAO: TemplateDAO) extends TemplateService {

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[List[Template]] = templateDAO.find(selector, sort)
  def insert(doc: Template): Future[WriteResult] = templateDAO.insert(doc)
  def findAndModify(selector: BSONDocument, modifier: Template, fields: BSONDocument): Future[Option[Template]] = templateDAO.findAndModify(selector, modifier, fields)
  def getList(page: Int, numberByPage: Int): Future[List[Template]] = templateDAO.getList(page, numberByPage)
  override def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = templateDAO.update(selector, modifier)
  override def remove(selector: BSONDocument): Future[WriteResult] = templateDAO.remove(selector)
  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Template]] = templateDAO.findOne(selector, sort)
  override def count(selector: JsObject): Future[Int] = templateDAO.count(selector)
}
