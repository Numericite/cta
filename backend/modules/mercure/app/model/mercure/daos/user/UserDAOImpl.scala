package model.mercure.daos.user

import java.util.UUID
import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import model.mercure.daos.mongo.MongoDAO
import play.api.Logger
import play.api.libs.json._
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import reactivemongo.api._
import reactivemongo.api.commands.{ Command, UpdateWriteResult, WriteResult }
import reactivemongo.bson.{ BSONDocument, BSONArray }
import reactivemongo.core.protocol.QueryFlags
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Give access to the user object.
 */
class UserDAOImpl @Inject() (
    reactiveMongoApi: ReactiveMongoApi,
    val authInfoRepository: AuthInfoRepository
) extends UserDAO with MongoDAO {

  val logger = Logger("MR." + this.getClass.getSimpleName);
  //def collection: JSONCollection = db.collection[JSONCollection]("user")
  //def collection: JSONCollection = db.collection[JSONCollection]("user")

  def collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("user"))
  def db: Future[DefaultDB] = reactiveMongoApi.database

  //def collectionPass: JSONCollection = db.collection[JSONCollection]("auth.info.password")
  //reactivemongo.play.json.collection.JSONCollection

  /**
   * Finds a user by its login info.
   *
   * @param loginInfo The login info of the user to find.
   * @return The found user or None if no user for the given login info could be found.
   */
  def find(loginInfo: LoginInfo, active: Option[Boolean]): Future[Option[User]] = {
    var selector = Json.obj("loginInfo" -> loginInfo)
    if (active.isDefined) {
      selector ++= Json.obj("active" -> true)
    }
    collection.flatMap(_.find(selector).one[User])
  }

  /**
   * Finds a user by its user ID.
   *
   * @param userID The ID of the user to find.
   * @return The found user or None if no user for the given ID could be found.
   */
  def find(userID: UUID): Future[Option[User]] = {
    collection.flatMap(_.find(Json.obj("userID" -> userID)).one[User])
  }

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(user: User): Future[User] = {
    onSuccess(collection.flatMap(_.update(BSONDocument("loginInfo" -> Json.toJson(user.loginInfo.get)), user, upsert = true)), user)
    /*
      flatMap {
      res =>
        res.inError match {
          case true => {
            logger.error(s"Error at user insert ====>${res.errmsg} code: ${res.code}")
            Future.successful(user)
          }
          case false => {
            logger.debug(s"INSERTED USER [ ${user.firstName} ]")
            Future.successful(user)
          }
        }
    }
    */
    // Future.successful(user)
  }

  def getByIds(userIds: List[String]): Future[Seq[User]] = {
    val query = Json.obj("userID" -> Json.obj("$in" -> userIds))
    collection.flatMap(_.find(query).cursor[User]().collect[List]())
  }

  def find(selector: BSONDocument, sort: JsObject = Json.obj("email" -> -1), page: Int = 1, numberPerPage: Int = 25): Future[List[User]] = {
    collection.flatMap(_.find(selector)
      .options(QueryOpts(skipN = ((page - 1) * numberPerPage), batchSizeN = numberPerPage, flagsN = 0))
      .sort(sort).cursor[User]().collect[List](numberPerPage))
  }

  def update(selector: BSONDocument, modifier: BSONDocument): Future[Option[User]] = {
    collection.flatMap(_.findAndUpdate(selector, modifier, fetchNewObject = true, upsert = false).map(_.result[User]))
  }

  def count(selector: BSONDocument): Future[Int] = {
    collection.flatMap(_.count(Some(selector.as[JsObject])))
  }
  def findAndModify(selector: BSONDocument, modifier: User, fields: BSONDocument): Future[Option[User]] = {
    collection.flatMap(_.findAndUpdate(selector, modifier, fetchNewObject = true, upsert = false).map(_.result[User]))
  }

  override def remove(selector: BSONDocument): Future[WriteResult] = {

    collection.flatMap(_.remove(selector))
    /*
    val ids = selector.getAs[BSONDocument]("userId").get.getAs[List[String]]("$in").get
    logger.debug(s"delete users: [$ids]")
    val findQuery = BSONDocument("userId" -> BSONDocument("$in" -> ids))
    collection.find(findQuery).cursor[User]().collect[List]().flatMap{
      users =>{
        val usersEmails = users.map( user => user.email)
      }
    }
    */
  }

  override def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult] = {
    collection.flatMap(_.update(selector, modifier, upsert = upsertIn, multi = true))
  }

  def getList(page: Int, numberByPage: Int): Future[List[User]] = {
    collection.flatMap(_.find(Json.obj()).options(QueryOpts(skipN = ((page - 1) * numberByPage), batchSizeN = numberByPage, flagsN = 0)).
      sort(Json.obj("created_date" -> -1))
      .cursor[User]().collect[List](numberByPage))
  }

  def aggregate(pipeLine: BSONArray): Future[JsObject] = {

    val commandQuery = BSONDocument(
      "aggregate" -> "user",
      "pipeline" -> pipeLine
    )

    val runner = Command.run(BSONSerializationPack)
    db.flatMap(db => {
      runner.apply(db, runner.rawCommand(commandQuery)).one[JsObject]
    })

  }

}

