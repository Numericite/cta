package services.mercure.category

import com.google.inject.Inject
import model.mercure.daos.category.{ Category, CategoryDAO }
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.DB
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.BSONDocument

import scala.concurrent.Future

/**
 * Created by madalien on 27/05/16.
 */
class CategoryServiceImpl @Inject() (categoryDAO: CategoryDAO) extends CategoryService {
  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[List[Category]] = categoryDAO.find(selector, sort)
  def insert(doc: Category): Future[WriteResult] = categoryDAO.insert(doc)
  def findAndModify(selector: BSONDocument, modifier: Category, fields: BSONDocument): Future[Option[Category]] = categoryDAO.findAndModify(selector, modifier, fields)
  def getList(page: Int, numberByPage: Int, sort: JsObject = Json.obj("updated_date" -> -1)): Future[List[Category]] = categoryDAO.getList(page, numberByPage, sort)
  override def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = categoryDAO.update(selector, modifier)
  override def remove(selector: BSONDocument): Future[WriteResult] = categoryDAO.remove(selector)
  override def count(selector: JsObject): Future[Int] = categoryDAO.count(selector)
}
