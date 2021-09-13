package models.daos.tag

/**
 * Created by Clément Lelong O8/05/17
 */
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

class TagDAOImpl @Inject() (reactiveMongoApi: ReactiveMongoApi) extends TagDAO {

  val logger = Logger("TM." + this.getClass.getSimpleName);
  def collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("tag"))
  def db: Future[DefaultDB] = reactiveMongoApi.database

  import TagFormats._
  import play.modules.reactivemongo.json._

  override def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = {
    collection.flatMap(_.update(selector, modifier, upsert = upsertIn, multi = true))
  }

  def findAndModify(selector: BSONDocument, modifier: Tag, fields: BSONDocument): Future[Option[Tag]] = {
    collection.flatMap(_.findAndUpdate(selector, modifier, fetchNewObject = true, upsert = false).map(_.result[Tag]))
  }

  override def remove(selector: BSONDocument): Future[WriteResult] = {
    collection.flatMap(_.remove(selector))
  }

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), projectionIn: BSONDocument = BSONDocument(), page: Int = 1, numberPerPage: Int = 25): Future[List[Tag]] = {
    collection.flatMap(_.find(selector)
      .options(QueryOpts(skipN = ((page - 1) * numberPerPage), batchSizeN = numberPerPage, flagsN = 0))
      .sort(sort).cursor[Tag]().collect[List](numberPerPage))
  }

  def insert(doc: Tag): Future[WriteResult] = {
    collection.flatMap(_.insert(doc))
  }

  def getList(page: Int, numberPerPage: Int): Future[List[Tag]] = {
    collection.flatMap(_.find(Json.obj())
      .options(QueryOpts(skipN = ((page - 1) * numberPerPage), batchSizeN = numberPerPage, flagsN = 0))
      .sort(Json.obj("updated_date" -> -1))
      .cursor[Tag]().collect[List](numberPerPage))
  }

  def count(selector: JsObject = Json.obj()): Future[Int] = {
    collection.flatMap(_.count(Some(selector)))
  }

  def findOne(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1)): Future[Option[Tag]] = {
    collection.flatMap(_.find(selector).sort(sort).one[Tag])
  }

  def aggregate(pipeLine: BSONArray): Future[JsObject] = {

    val commandQuery = BSONDocument(
      "aggregate" -> "tag",
      "pipeline" -> pipeLine
    )

    val runner = Command.run(BSONSerializationPack)
    db.flatMap(db => {
      runner.apply(db, runner.rawCommand(commandQuery)).one[JsObject]
    })

  }

}
