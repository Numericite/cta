package models.daos.location

import com.google.inject.Inject
import org.joda.time.DateTime
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{ JsObject, Json }
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.{ BSONSerializationPack, DefaultDB, QueryOpts }
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONArray, BSONDocument }
import reactivemongo.play.json.collection.JSONCollection
import reactivemongo.api.commands.{ Command, UpdateWriteResult, WriteResult }

import scala.concurrent.Future

/**
 * Created by Clément Lelong O8/05/17
 */
class LocationDAOImpl @Inject() (reactiveMongoApi: ReactiveMongoApi) extends LocationDAO {

  val logger = Logger("IDP." + this.getClass.getSimpleName);
  def collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("location"))
  def db: Future[DefaultDB] = reactiveMongoApi.database

  import Location._
  import play.modules.reactivemongo.json._

  override def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = {
    collection.flatMap(_.update(selector, modifier, upsert = upsertIn))
  }

  def findAndModify(selector: BSONDocument, modifier: Location, fields: BSONDocument): Future[Option[Location]] = {
    collection.flatMap(_.findAndUpdate(selector, modifier, fetchNewObject = true, upsert = false).map(_.result[Location]))
  }

  override def remove(selector: BSONDocument): Future[WriteResult] = {
    collection.flatMap(_.remove(selector))
  }

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), page: Int = 1, numberPerPage: Int = 25): Future[List[Location]] = {
    collection.flatMap(_.find(selector).
      sort(sort)
      .options(QueryOpts(skipN = ((page - 1) * numberPerPage), batchSizeN = numberPerPage, flagsN = 0))
      .cursor[Location]().collect[List](numberPerPage))
  }

  def insert(doc: Location): Future[WriteResult] = {
    val docWithId = doc.copy(updated_date = Some(new DateTime()))
    collection.flatMap(_.insert(docWithId))
  }

  def getList(page: Int, numberPerPage: Int): Future[List[Location]] = {
    collection.flatMap(_.find(Json.obj()).options(QueryOpts(skipN = ((page - 1) * numberPerPage), batchSizeN = numberPerPage, flagsN = 0))
      .sort(Json.obj("updated_date" -> -1))
      .cursor[Location]().collect[List](numberPerPage))
  }

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Location]] = {
    collection.flatMap(_.find(selector).sort(sort).one[Location])
  }

  def aggregate(pipeLine: BSONArray): Future[JsObject] = {

    val commandQuery = BSONDocument(
      "aggregate" -> "location",
      "pipeline" -> pipeLine
    )

    val runner = Command.run(BSONSerializationPack)
    db.flatMap(db => {
      runner.apply(db, runner.rawCommand(commandQuery)).one[JsObject]
    })

  }
}