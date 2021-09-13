
package model.mercure.daos.status

import java.util.UUID

import com.google.inject.Inject
import org.joda.time.DateTime
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{ JsObject, Json }
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.api.{ DB, QueryOpts }
import reactivemongo.bson.BSONDocument
import reactivemongo.core.protocol.QueryFlags
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future

/**
 * Created by madalien on 23/05/16.
 */
class StatusDAOImpl @Inject() (reactiveMongoApi: ReactiveMongoApi) extends StatusDAO {

  val logger = Logger("MR." + this.getClass.getSimpleName);

  def collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("status"))
  import Status._
  import play.modules.reactivemongo.json._

  override def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = {
    collection.flatMap(_.update(selector, modifier, upsert = upsertIn))
  }

  def findAndModify(selector: BSONDocument, modifier: Status, fields: BSONDocument): Future[Option[Status]] = {
    collection.flatMap(_.findAndUpdate(selector, modifier, fetchNewObject = true, upsert = false).map(_.result[Status]))
  }

  override def remove(selector: BSONDocument): Future[WriteResult] = {
    collection.flatMap(_.remove(selector))
  }

  def find(selector: BSONDocument, sort: JsObject): Future[List[Status]] = {
    collection.flatMap(_.find(selector).sort(sort).cursor[Status]().collect[List]())
  }

  def insert(doc: Status): Future[WriteResult] = {
    val docWithId = doc.copy(updated_date = Some(new DateTime()))
    collection.flatMap(_.insert(docWithId))
  }

  def getList(page: Int, numberByPage: Int): Future[List[Status]] = {
    collection.flatMap(_.find(Json.obj()).options(QueryOpts(skipN = ((page - 1) * numberByPage), batchSizeN = numberByPage, flagsN = 0)).
      sort(Json.obj("updated_date" -> -1))
      .cursor[Status]().collect[List](numberByPage))
  }

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Status]] = {
    collection.flatMap(_.find(selector).sort(sort).one[Status])
  }

  def count(selector: JsObject): Future[Int] = {
    collection.flatMap(_.count(Some(selector)))
  }
}
