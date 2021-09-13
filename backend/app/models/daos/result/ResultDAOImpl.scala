package models.daos.result

import com.google.inject.Inject
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Writes._
import play.api.libs.json.{ JsObject, Json }
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json.collection
import reactivemongo.api.commands.{ Command, MultiBulkWriteResult, UpdateWriteResult, WriteResult }
import reactivemongo.api.{ BSONSerializationPack, DefaultDB, QueryOpts }
import reactivemongo.bson.{ BSONArray, BSONDocument }
import reactivemongo.play.json.collection.JSONCollection
import reactivemongo.api.commands.bson.DefaultBSONCommandError

import scala.concurrent.Future

/**
 * Created by Clément Lelong on 23/05/16.
 */
class ResultDAOImpl @Inject() (reactiveMongoApi: ReactiveMongoApi) extends ResultDAO {

  val logger = Logger("BP." + this.getClass.getSimpleName);
  def collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("result"))
  def db: Future[DefaultDB] = reactiveMongoApi.database

  import ResultFormats._
  import play.modules.reactivemongo.json._

  override def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = {
    collection.flatMap(_.update(selector, modifier, upsert = upsertIn, multi = true))
  }

  def findAndModify(selector: BSONDocument, modifier: Result, fields: BSONDocument): Future[Option[Result]] = {
    collection.flatMap(_.findAndUpdate(selector, modifier, fetchNewObject = true, upsert = false).map(_.result[Result]))
  }

  override def remove(selector: BSONDocument): Future[WriteResult] = {
    collection.flatMap(_.remove(selector))
  }

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Result]] = {
    collection.flatMap(_.find(selector)
      .options(QueryOpts(skipN = ((page - 1) * numberPerPage), batchSizeN = numberPerPage, flagsN = 0))
      .sort(sort).cursor[Result]().collect[List](numberPerPage))
  }

  def insert(doc: Result): Future[WriteResult] = {
    collection.flatMap(_.insert(doc))
  }

  def insertMany(docs: List[Result]): Future[MultiBulkWriteResult] = {
    collection.flatMap(collection => {
      for {
        writeResult <- collection.bulkInsert(ordered = false)(docs.map(implicitly[collection.ImplicitlyDocumentProducer](_)): _*)
      } yield {
        writeResult
      }
    })
  }

  def getList(page: Int, numberPerPage: Int): Future[List[Result]] = {
    collection.flatMap(_.find(Json.obj())
      .options(QueryOpts(skipN = ((page - 1) * numberPerPage), batchSizeN = numberPerPage, flagsN = 0))
      .sort(Json.obj("updated_date" -> -1))
      .cursor[Result]().collect[List](numberPerPage))
  }

  def count(selector: JsObject = Json.obj()): Future[Int] = {
    collection.flatMap(_.count(Some(selector)))
  }

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Result]] = {
    collection.flatMap(_.find(selector).sort(sort).one[Result])
  }

  def aggregate(pipeLine: BSONArray): Future[JsObject] = {

    val commandQuery = BSONDocument(
      "aggregate" -> "result",
      "pipeline" -> pipeLine
    )

    val runner = Command.run(BSONSerializationPack)
    db.flatMap(db => {
      runner.apply(db, runner.rawCommand(commandQuery)).one[JsObject]
    })

  }

}
