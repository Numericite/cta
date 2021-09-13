package model.mercure.daos.token

import com.google.inject.Inject
import model.mercure.daos.mongo.MongoDAO
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.DB
import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future

/**
 * Created by madalien on 09/06/16.
 */
class TokenDAOImpl @Inject() (reactiveMongoApi: ReactiveMongoApi) extends TokenDAO with MongoDAO {

  val logger = Logger("MR." + this.getClass.getSimpleName);
  def collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("token"))
  import Token._
  import play.modules.reactivemongo.json._

  def insert(doc: Token): Future[WriteResult] = {
    collection.flatMap(_.insert(doc))
  }

  def update(selector: BSONDocument, docModifier: Token, upsertIn: Boolean = false): Future[UpdateWriteResult] = {
    collection.flatMap(_.update(selector, docModifier, upsert = upsertIn))
  }

  def findById(id: String): Future[Option[Token]] = {
    collection.flatMap(_.find(BSONDocument("id" -> id)).one[Token])
  }

  def remove(selector: BSONDocument): Future[WriteResult] = {
    collection.flatMap(_.remove(selector))
  }

}
