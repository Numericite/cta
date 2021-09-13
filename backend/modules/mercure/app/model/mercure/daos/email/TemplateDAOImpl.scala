
package model.mercure.daos.email

import com.google.inject.Inject
import org.joda.time.DateTime
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{ JsObject, Json }
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.QueryOpts
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future

/**
 * Created by madalien on 23/05/16.
 */
class TemplateDAOImpl @Inject() (reactiveMongoApi: ReactiveMongoApi) extends TemplateDAO {

  val logger = Logger("MR." + this.getClass.getSimpleName);

  def collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("template"))
  import play.modules.reactivemongo.json._

  override def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = {
    collection.flatMap(_.update(selector, modifier, upsert = upsertIn))
  }

  def findAndModify(selector: BSONDocument, modifier: Template, fields: BSONDocument): Future[Option[Template]] = {
    collection.flatMap(_.findAndUpdate(selector, modifier, fetchNewObject = true, upsert = false).map(_.result[Template]))
  }

  override def remove(selector: BSONDocument): Future[WriteResult] = {
    collection.flatMap(_.remove(selector))
  }

  def find(selector: BSONDocument, sort: JsObject): Future[List[Template]] = {
    collection.flatMap(_.find(selector).sort(sort).cursor[Template]().collect[List]())
  }

  def insert(doc: Template): Future[WriteResult] = {
    val docWithId = doc.copy(updated_date = Some(new DateTime()))
    collection.flatMap(_.insert(docWithId))
  }

  def getList(page: Int, numberByPage: Int): Future[List[Template]] = {
    collection.flatMap(_.find(Json.obj()).options(QueryOpts(skipN = ((page - 1) * numberByPage), batchSizeN = numberByPage, flagsN = 0)).
      sort(Json.obj("updated_date" -> -1))
      .cursor[Template]().collect[List](numberByPage))
  }

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Template]] = {
    collection.flatMap(_.find(selector).sort(sort).one[Template])
  }

  def count(selector: JsObject): Future[Int] = {
    collection.flatMap(_.count(Some(selector)))
  }
}
