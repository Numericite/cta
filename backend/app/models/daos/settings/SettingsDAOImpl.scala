package models.daos.settings

/**
 * Created by madalien on 12/19/16.
 */

import com.google.inject.Inject
import models.daos.settings.Settings
import org.joda.time.DateTime
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{ JsObject, Json }
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.commands.{ Command, UpdateWriteResult, WriteResult }
import reactivemongo.api.{ BSONSerializationPack, DefaultDB, QueryOpts }
import reactivemongo.bson.{ BSONArray, BSONDocument }
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future

class SettingsDAOImpl @Inject() (reactiveMongoApi: ReactiveMongoApi) extends SettingsDAO {

  val logger = Logger("BP." + this.getClass.getSimpleName);

  import SettingsFormats._

  def collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("settings"))
  def db: Future[DefaultDB] = reactiveMongoApi.database

  import play.modules.reactivemongo.json._
  override def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = {
    collection.flatMap(_.update(selector, modifier, upsert = upsertIn))
  }

  def findAndModify(selector: BSONDocument, modifier: Settings, fields: BSONDocument): Future[Option[Settings]] = {
    collection.flatMap(_.findAndUpdate(selector, modifier, fetchNewObject = true, upsert = false).map(_.result[Settings]))
  }

  override def remove(selector: BSONDocument): Future[WriteResult] = {
    collection.flatMap(_.remove(selector))
  }

  def find(selector: BSONDocument, sort: JsObject = Json.obj("updated_date" -> -1), page: Int = 1, numberByPage: Int = 25): Future[List[Settings]] = {
    collection.flatMap(_.find(selector)
      .sort(sort)
      .options(QueryOpts(skipN = ((page - 1) * numberByPage), batchSizeN = numberByPage, flagsN = 0))
      .cursor[Settings]().collect[List](numberByPage))
  }

  def count(selector: BSONDocument): Future[Int] = {
    collection.flatMap(_.count(Some(selector.as[JsObject])))
  }

  def insert(doc: Settings): Future[WriteResult] = {
    val docWithId = doc.copy(updated_date = Some(new DateTime()))
    collection.flatMap(_.insert(docWithId))
  }

  def distinct(distinctKey: String, selector: JsObject): Future[List[String]] = {
    collection.flatMap(
      _.distinct[String, List](distinctKey, Some(selector))
    )
  }

  def getList(page: Int, numberByPage: Int): Future[List[Settings]] = {
    collection.flatMap(_.find(Json.obj()).options(QueryOpts(skipN = ((page - 1) * numberByPage), batchSizeN = numberByPage, flagsN = 0)).
      sort(Json.obj("updated_date" -> -1))
      .cursor[Settings]().collect[List](numberByPage))
  }

  def aggregate(pipeLine: BSONArray): Future[JsObject] = {

    val commandQuery = BSONDocument(
      "aggregate" -> "settings",
      "pipeline" -> pipeLine
    )

    val runner = Command.run(BSONSerializationPack)
    db.flatMap(db => {
      runner.apply(db, runner.rawCommand(commandQuery)).one[JsObject]
    })

  }

}
