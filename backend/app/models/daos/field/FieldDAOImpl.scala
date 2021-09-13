package models.daos.field

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
class FieldDAOImpl @Inject() (reactiveMongoApi: ReactiveMongoApi) extends FieldDAO {

  val logger = Logger("BP." + this.getClass.getSimpleName);
  def collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("field"))
  def db: Future[DefaultDB] = reactiveMongoApi.database

  import FieldFormats._
  import play.modules.reactivemongo.json._

  override def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = {
    collection.flatMap(_.update(selector, modifier, upsert = upsertIn, multi = true))
  }

  def findAndModify(selector: BSONDocument, modifier: Field, fields: BSONDocument): Future[Option[Field]] = {
    collection.flatMap(_.findAndUpdate(selector, modifier, fetchNewObject = true, upsert = false).map(_.result[Field]))
  }

  override def remove(selector: BSONDocument): Future[WriteResult] = {
    collection.flatMap(_.remove(selector))
  }

  def find(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Field]] = {
    collection.flatMap(_.find(selector)
      .options(QueryOpts(skipN = ((page - 1) * numberPerPage), batchSizeN = numberPerPage, flagsN = 0))
      .sort(sort).cursor[Field]().collect[List](numberPerPage))
  }

  def insert(doc: Field): Future[WriteResult] = {
    collection.flatMap(_.insert(doc))
  }

  def getList(page: Int, numberPerPage: Int): Future[List[Field]] = {
    collection.flatMap(_.find(Json.obj())
      .options(QueryOpts(skipN = ((page - 1) * numberPerPage), batchSizeN = numberPerPage, flagsN = 0))
      .sort(Json.obj("num" -> 1))
      .cursor[Field]().collect[List](numberPerPage))
  }

  def count(selector: JsObject = Json.obj()): Future[Int] = {
    collection.flatMap(_.count(Some(selector)))
  }

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("num" -> 1)): Future[Option[Field]] = {
    collection.flatMap(_.find(selector).sort(sort).one[Field])
  }

  def aggregate(pipeLine: BSONArray): Future[JsObject] = {

    val commandQuery = BSONDocument(
      "aggregate" -> "field",
      "pipeline" -> pipeLine
    )

    val runner = Command.run(BSONSerializationPack)
    db.flatMap(db => {
      runner.apply(db, runner.rawCommand(commandQuery)).one[JsObject]
    })

  }

}
