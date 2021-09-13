package model.mercure.daos.category

/**
 * Created by madalien on 27/05/16.
 */

import akka.event.slf4j.Logger
import com.google.inject.Inject
import org.joda.time.DateTime
import play.api.libs.json.{ Json, JsObject }
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.{ QueryOpts, DB }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.BSONDocument
import reactivemongo.core.protocol.QueryFlags
import reactivemongo.play.json.collection.JSONCollection
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.Future

class CategoryDAOImpl @Inject() (reactiveMongoApi: ReactiveMongoApi) extends CategoryDAO {

  val logger = Logger("MR." + this.getClass.getSimpleName);
  def collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("category"))

  import play.modules.reactivemongo.json._
  import Category._
  override def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = {
    collection.flatMap(_.update(selector, modifier, upsert = upsertIn))
  }

  def findAndModify(selector: BSONDocument, modifier: Category, fields: BSONDocument): Future[Option[Category]] = {
    collection.flatMap(_.findAndUpdate(selector, modifier, fetchNewObject = true, upsert = false).map(_.result[Category]))
  }

  override def remove(selector: BSONDocument): Future[WriteResult] = {
    collection.flatMap(_.remove(selector))
  }

  def find(selector: BSONDocument, sort: JsObject): Future[List[Category]] = {
    collection.flatMap(_.find(selector).sort(sort).cursor[Category]().collect[List]())
  }

  def insert(doc: Category): Future[WriteResult] = {
    val docWithId = doc.copy(updated_date = Some(new DateTime()))
    collection.flatMap(_.insert(docWithId))
  }

  def getList(page: Int, numberByPage: Int, sort: JsObject = Json.obj("updated_date" -> -1)): Future[List[Category]] = {
    collection.flatMap(_.find(Json.obj()).options(QueryOpts(skipN = ((page - 1) * numberByPage), batchSizeN = numberByPage, flagsN = 0)).
      sort(sort)
      .cursor[Category]().collect[List](numberByPage))

  }

  def count(selector: JsObject): Future[Int] = {
    collection.flatMap(_.count(Some(selector)))
  }

}

