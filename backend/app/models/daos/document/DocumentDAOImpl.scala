package models.daos.document

import com.google.inject.Inject
import org.joda.time.DateTime
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{ JsObject, Json }
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api._
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future

/**
 * Created by madalien on 23/05/16.
 */
class DocumentDAOImpl @Inject() (reactiveMongoApi: ReactiveMongoApi) extends DocumentDAO {

  val logger = Logger("CC." + this.getClass.getSimpleName);
  def collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("document"))

  import play.modules.reactivemongo.json._

  override def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = {
    collection.flatMap(_.update(selector, modifier, upsert = upsertIn))
  }

  def findAndModify(selector: BSONDocument, modifier: Document, fields: BSONDocument, upsertIn: Boolean = false): Future[Option[Document]] = {
    collection.flatMap(_.findAndUpdate(selector, modifier, fetchNewObject = true, upsert = upsertIn).map(_.result[Document]))
  }

  override def remove(selector: BSONDocument): Future[WriteResult] = {
    collection.flatMap(_.remove(selector))
  }

  def find(selector: BSONDocument, sort: JsObject, page: Int, numberPerPage: Int): Future[List[Document]] = {
    collection.flatMap(
      _.find(selector).
        sort(sort).
        options(QueryOpts(skipN = ((page - 1) * numberPerPage), batchSizeN = numberPerPage, flagsN = 0)).
        cursor[Document]().
        collect[List](numberPerPage)
    )
  }

  def count(selector: JsObject = Json.obj()): Future[Int] = {
    collection.flatMap(_.count(Some(selector)))
  }

  def insert(doc: Document): Future[WriteResult] = {
    val docWithId = doc.copy(updated_date = Some(new DateTime()))
    collection.flatMap(_.insert(docWithId))
  }

  def getList(page: Int, numberPerPage: Int): Future[List[Document]] = {
    collection.flatMap(_.find(Json.obj())
      .options(QueryOpts(skipN = ((page - 1) * numberPerPage), batchSizeN = numberPerPage, flagsN = 0))
      .sort(Json.obj("updated_date" -> -1))
      .cursor[Document]().collect[List](numberPerPage))
  }
}

