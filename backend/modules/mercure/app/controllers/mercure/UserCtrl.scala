package controllers.mercure

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.{ LoginInfo, Silhouette }
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.typesafe.config.ConfigFactory
import fly.play.s3.{ BucketFile, S3, S3Exception }
import model.mercure.daos.user.User
import org.apache.commons.io.FileUtils
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.api.DB
import reactivemongo.bson.{ BSON, BSONArray, BSONDocument }
import services.mercure.user.UserService
import utils.mercure.DefaultEnv
import utils.mercure.auth.{ IsOwner, WithRoles }
import play.api.libs.concurrent.Execution.Implicits._
import net.ceedubs.ficus.Ficus._

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

/**
 * Created by madalien on 23/05/16.
 */

class UserCtrl @Inject() (
    val messagesApi: MessagesApi,
    val userService: UserService,
    implicit val application: play.api.Application,
    passwordHasher: PasswordHasher,
    authInfoRepository: AuthInfoRepository,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("MR." + this.getClass.getSimpleName);

  import play.api.libs.json.Json

  import reactivemongo.play.json.BSONFormats._
  import model.mercure.daos.bodyparser.SignUpFormat._
  import model.mercure.daos.bodyparser.SignIn._
  def updateUser(email: String) = silhouette.SecuredAction(IsOwner(email)).async(parse.json[User]) {

    implicit request =>
      {

        implicit val loginInfoFormat = Json.format[LoginInfo]
        val loginInfo = request.identity.loginInfo.get
        val selector = BSONDocument("loginInfo" -> Json.toJson(loginInfo))
        userService.find(selector).flatMap {
          curUser =>
            {
              val userInfos = curUser.head
              val elemToUpdate = request.body.copy(updated_date = Some(new DateTime()), roles = userInfos.roles)
              logger.debug(s"User update: $elemToUpdate")
              userService.findAndModify(selector, elemToUpdate.copy(), BSONDocument()) map {
                mayOldUsr =>
                  {
                    mayOldUsr match {
                      case Some(oldUsr: User) => {
                        Ok(Json.obj("res" -> Json.toJson(oldUsr)))
                      }
                      case _ => InternalServerError(Json.obj("res" -> s"error while updating user $elemToUpdate"))
                    }
                  }
              }
            }
        }
      }
  }

  def getByEmail(email: String) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        userService.find(BSONDocument("email" -> email)).map {
          usrRes => Ok(Json.toJson(usrRes))
        }
      }
  }

  def searchByFullName(fullName: String) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val aggregateQuery = BSONArray(
          BSONDocument(
            "$project" -> BSONDocument(
              "fullName" -> BSONDocument("$concat" -> BSONArray("$firstName", " ", "$lastName", " (", "$email", ")")),
              "email" -> true,
              "userID" -> true
            )
          ),
          BSONDocument(
            "$match" -> BSONDocument(
              "fullName" -> BSONDocument("$regex" -> fullName, "$options" -> "i")
            )
          )
        )

        userService.aggregate(aggregateQuery).map {
          result => Ok(result)
        }
      }
  }

  def search(email: Option[String], firstName: Option[String], lastName: Option[String], page: Option[Int], numberPerPage: Option[Int]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {

        var selector = BSONDocument()

        if (email.isDefined) {
          selector ++= BSONDocument("email" -> BSONDocument("$regex" -> email.get, "$options" -> "i"))
        }

        if (firstName.isDefined) {
          selector ++= BSONDocument("firstName" -> BSONDocument("$regex" -> firstName.get, "$options" -> "i"))
        }

        if (lastName.isDefined) {
          selector ++= BSONDocument("lastName" -> BSONDocument("$regex" -> lastName.get, "$options" -> "i"))
        }

        userService.find(selector, Json.obj("updated_date" -> -1), page.getOrElse(1), numberPerPage.getOrElse(25)).map {
          usrArr => Ok(Json.toJson(usrArr))
        }
      }
  }

  def count(active: Option[Boolean], isValidated: Option[Boolean]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {

        var query = BSONDocument()

        if (active.nonEmpty) {
          query ++= BSONDocument("active" -> active)
        }

        if (isValidated.nonEmpty) {
          query ++= BSONDocument("password_info" -> BSONDocument("$exists" -> !isValidated.get))
        }

        userService.count(query).map {
          nbUsr => Ok(Json.obj("res" -> nbUsr))
        }
      }
  }

  def updateUserAdmin(id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[User]) {

    implicit request =>
      {

        val elemToUpdate = request.body.copy(updated_date = Some(new DateTime()))
        val selector = BSONDocument("userID" -> id);
        logger.debug(s"$elemToUpdate")
        userService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldUsr =>
            {
              mayOldUsr match {
                case Some(oldUsr: User) => {
                  Ok(Json.obj("res" -> Json.toJson(oldUsr)))
                }
                case _ => InternalServerError(Json.obj("res" -> s"error while updating user $mayOldUsr"))
              }
            }
        }
      }
  }

  def userList(page: Int, numberByPage: Int, email: Option[String], firstName: Option[String], lastName: Option[String], active: Option[Boolean], isValidated: Option[Boolean], sotByFirstName: Option[Int], sortByLastName: Option[Int], accountType: Option[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        var query = BSONDocument()
        var sort = Json.obj()

        if (active.nonEmpty) {
          query ++= BSONDocument("active" -> active)
        }

        if (isValidated.nonEmpty) {
          query ++= BSONDocument("password_info" -> BSONDocument("$exists" -> !isValidated.get))
        }

        if (accountType.isDefined) {
          query ++= BSONDocument("config.accountType" -> accountType.get)
        }

        if (email.isDefined && firstName.isDefined && lastName.isDefined) {
          query ++= BSONDocument(
            "$or" -> BSONArray(
              BSONDocument("email" -> BSONDocument("$regex" -> email.get, "$options" -> "i")),
              BSONDocument("firstName" -> BSONDocument("$regex" -> firstName.get, "$options" -> "i")),
              BSONDocument("lastName" -> BSONDocument("$regex" -> lastName.get, "$options" -> "i"))
            )
          )
        } else {
          if (email.isDefined) {
            query ++= BSONDocument("email" -> BSONDocument("$regex" -> email, "$options" -> "i"))
          }

          if (firstName.isDefined) {
            query ++= BSONDocument("firstName" -> BSONDocument("$regex" -> firstName, "$options" -> "i"))
          }

          if (lastName.isDefined) {
            query ++= BSONDocument("lastName" -> BSONDocument("$regex" -> lastName, "$options" -> "i"))
          }
        }

        if (sortByLastName.isDefined) {
          sort = Json.obj("lastName" -> sortByLastName.get)
        } else if (sotByFirstName.isDefined) {
          sort = Json.obj("firstName" -> sotByFirstName.get)
        } else {
          sort = Json.obj("created_date" -> -1)
        }

        userService.find(query, sort, page, numberByPage) map {
          userArr =>
            {
              Ok(Json.toJson(userArr))
            }
        }
      }
  }

  def getUserByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        logger.info(s"$ids")
        userService.getByIds(ids).map {
          subArr =>
            {

              var userWithoutEmail: Seq[User] = List()
              if (request.identity.roles.contains("Administrator")) {
                userWithoutEmail = subArr
              } else {
                userWithoutEmail = subArr.map {
                  user =>
                    {
                      val configOut = user.config.fold(BSONDocument()) {

                        config =>
                          {

                            var configTmp = config.remove("address")
                            configTmp = configTmp.remove("phoneNumber")
                            configTmp = configTmp.remove("phone")
                            configTmp = configTmp.remove("birthDate")
                            configTmp.add(BSONDocument("isAdmin" -> user.roles.contains("Administrator")))
                          }
                      }
                      val rolesOut = List[String]()
                      user.copy(roles = rolesOut, config = Some(configOut), email = "", loginInfo = None)
                    }
                }

              }
              Ok(Json.toJson(userWithoutEmail))
            }
        }
      }
  }

  def userByIdsIDP(ids: List[String]) = Action.async {
    implicit request =>
      {
        logger.info(s"$ids")
        userService.getByIds(ids).map {
          subArr =>
            {

              val userWithoutEmail = subArr.map {
                user =>
                  {

                    val configOut = user.config.fold(BSONDocument()) {

                      config =>
                        {
                          var configTmp = config.remove("address")
                          configTmp = configTmp.remove("phoneNumber")
                          configTmp = configTmp.remove("phone")
                          configTmp = configTmp.remove("birthDate")
                          configTmp.add(BSONDocument("isAdmin" -> user.roles.contains("Administrator")))
                        }
                    }
                    val rolesOut = List[String]()
                    user.copy(roles = rolesOut, config = Some(configOut), email = "", loginInfo = None)
                  }
              }
              Ok(Json.toJson(userWithoutEmail))
            }
        }
      }
  }

  // refactoring in findone

  /*


def resetPassword(ids: List[String]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        logger.info(s"$ids")
        userService.getByIds(ids).map {
          subArr => Ok(Json.toJson(subArr))
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = BSONDocument("userID" -> BSONDocument("$in" -> ids))
        userService.remove(query).map {
          res =>
            res.inError match {
              case true => {
                InternalServerError(Json.obj("res" -> s" ${res.message}  code: ${res.code}"))
              }
              case false => {
                Ok(Json.obj("res" -> s" ${res.message} Remove Done"))
              }
            }
        }
      }
  }


def findUserById(id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
implicit request =>
{
  val query = BSONDocument("userID" -> id)
  userService.find(query).map {
    subArr => Ok(Json.toJson(subArr))
  }
        Future.successful(Ok(s"nun nun"))
      }

  }

  def byAtelier(id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = BSONDocument("ateliers" -> id)
        userService.find(query).map {
          subArr => Ok(Json.toJson(subArr))
        }
      }
  }

  */

  def validate(email: String) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("email" -> email)
        userService.find(query).map {
          subArr => if (subArr.isEmpty) NotFound else Ok
        }
      }
  }

  def uploadLogo(user_id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.multipartFormData) {
    request =>
      request.body.file("file").map { file =>
        import java.io.File

        val currentPath = application.path.getAbsolutePath
        val filename = file.filename.replace(' ', '_')
        val filePath = currentPath + "/user/" + user_id + "/" + filename;
        val fileUrl = "logos/user/" + user_id + "/" + filename;
        val contentType = file.contentType
        //val title = request.body.asFormUrlEncoded.get("title").get(0);
        val dir = new File(currentPath + "/user/" + user_id)
        dir.mkdirs()
        logger.debug(s"Application cur path [$currentPath]")
        val fileFromDisk = file.ref.moveTo(new File(filePath))

        val bucketName = ConfigFactory.load().as[String]("s3.bucketName")
        val bucket = S3(bucketName)
        val byteArr = FileUtils.readFileToByteArray(fileFromDisk)
        val result = bucket add BucketFile(fileUrl, file.contentType.getOrElse("text/plain"), byteArr, None, None)
        result.flatMap {
          unit =>
            {
              val awsFileUrl = bucket.s3.url(bucketName, fileUrl)
              logger.debug(s"Aws result $unit")

              val updatedAtelier = BSONDocument(
                "$set" -> BSONDocument(
                  "avatar_path" -> awsFileUrl
                )
              )

              val selec = BSONDocument("userID" -> user_id);
              userService.update(selec, updatedAtelier, upsertIn = false) map {
                res =>
                  res.inError match {
                    case true => {
                      InternalServerError(Json.obj("res" -> s" ${res.message}  code: ${res.code}"))
                    }
                    case false => {
                      Ok(Json.obj("res" -> Json.obj("avatar_path" -> awsFileUrl)))
                    }
                  }
              }

            }
        }.recover {
          case S3Exception(status, code, message, originalXml) =>
            {
              logger.error("Error: " + message)
              BadRequest(Json.obj("result" -> "main logo error: missing file or existing file"))
            }
        }

      }.getOrElse {
        Future.successful(BadRequest(Json.obj("result" -> "atelier error: missing file or existing file")))
      }
  }

  def getLogo(filename: String) = Action {
    implicit request =>
      {
        Ok.sendFile(new java.io.File(application.path.getAbsolutePath + "/" + filename))
      }
  }

}
