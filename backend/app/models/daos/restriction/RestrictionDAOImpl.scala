package models.daos.restriction

import com.google.inject.Inject
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Writes._
import play.api.libs.json.{ JsObject, Json }
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.commands.{ Command, UpdateWriteResult, WriteResult }
import reactivemongo.api.{ BSONSerializationPack, DefaultDB, QueryOpts }
import reactivemongo.bson.{ BSONArray, BSONDocument }
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future

/**
 * Created by Clément Lelong on 23/05/16.
 */
class RestrictionDAOImpl @Inject() (reactiveMongoApi: ReactiveMongoApi) extends RestrictionDAO {

  val logger = Logger("BP." + this.getClass.getSimpleName);

  def collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("restriction"))

  def db: Future[DefaultDB] = reactiveMongoApi.database

  import RestrictionFormats._
  import play.modules.reactivemongo.json._

  override def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = {
    collection.flatMap(_.update(selector, modifier, upsert = upsertIn, multi = true))
  }

  def findAndModify(selector: BSONDocument, modifier: Restriction, fields: BSONDocument): Future[Option[Restriction]] = {
    collection.flatMap(_.findAndUpdate(selector, modifier, fetchNewObject = true, upsert = false).map(_.result[Restriction]))
  }

  override def remove(selector: BSONDocument): Future[WriteResult] = {
    collection.flatMap(_.remove(selector))
  }

  def find(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Restriction]] = {
    collection.flatMap(_.find(selector)
      .options(QueryOpts(skipN = ((page - 1) * numberPerPage), batchSizeN = numberPerPage, flagsN = 0))
      .sort(sort).cursor[Restriction]().collect[List](numberPerPage))
  }

  def insert(doc: Restriction): Future[WriteResult] = {
    collection.flatMap(_.insert(doc))
  }

  def getList(page: Int, numberPerPage: Int): Future[List[Restriction]] = {
    collection.flatMap(_.find(Json.obj())
      .options(QueryOpts(skipN = ((page - 1) * numberPerPage), batchSizeN = numberPerPage, flagsN = 0))
      .sort(Json.obj("num" -> 1))
      .cursor[Restriction]().collect[List](numberPerPage))
  }

  def count(selector: JsObject = Json.obj()): Future[Int] = {
    collection.flatMap(_.count(Some(selector)))
  }

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1)): Future[Option[Restriction]] = {
    collection.flatMap(_.find(selector).sort(sort).one[Restriction])
  }

  def aggregate(pipeLine: BSONArray): Future[JsObject] = {

    val commandQuery = BSONDocument(
      "aggregate" -> "restriction",
      "pipeline" -> pipeLine
    )

    val runner = Command.run(BSONSerializationPack)
    db.flatMap(db => {
      runner.apply(db, runner.rawCommand(commandQuery)).one[JsObject]
    })

  }

}
