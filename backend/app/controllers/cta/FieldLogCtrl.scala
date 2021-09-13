package controllers.cta

import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import models.daos.fieldlog.FieldLog
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.fieldlog.FieldLogService
import services.mercure.user.UserService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

/**
 * Created by Clement LELONG on 13/06/19.
 */

class FieldLogCtrl @Inject() (
    val messagesApi: MessagesApi,
    val userService: UserService,
    val fieldLogService: FieldLogService,
    implicit val application: play.api.Application,
    passwordHasher: PasswordHasher,
    authInfoRepository: AuthInfoRepository,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("MR." + this.getClass.getSimpleName);

  import models.daos.fieldlog.FieldLogFormats._

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[FieldLog]) {
    implicit request =>
      {
        val fieldLog = request.body
        val uid = UUID.randomUUID()

        val docWithId = fieldLog.copy(id = Some(uid), created_date = Some(new DateTime()), updated_date = Some(new DateTime()))
        fieldLogService.insert(docWithId).map {
          res =>
            res.inError match {
              case true => {
                InternalServerError(Json.obj("res" -> s" ${res.message}  code: ${res.code}"))
              }
              case false => {
                Created(Json.obj("res" -> Json.obj("id" -> uid.toString)))
              }
            }
        }

      }
  }

  def update(id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[FieldLog]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        fieldLogService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldFieldLog =>
            {
              mayOldFieldLog match {
                case Some(oldFieldLog: FieldLog) => {
                  Ok(Json.obj("res" -> "Module successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating module"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        fieldLogService.remove(query).map {
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

  def getList(
    page: Int,
    numberPerPage: Int,
    field_ids: Option[List[String]],
    user_ids: Option[List[String]],
    child_id: Option[String],
    child_type: Option[String]
  ) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        var query = BSONDocument()

        if (field_ids.get.nonEmpty) {
          query ++= BSONDocument("field_id" -> BSONDocument("$in" -> field_ids))
        }

        if (user_ids.get.nonEmpty) {
          query ++= BSONDocument("user_id" -> BSONDocument("$in" -> user_ids))
        }

        if (child_id.isDefined) {
          query ++= BSONDocument("child_id" -> child_id)
        }

        if (child_type.isDefined) {
          query ++= BSONDocument("child_type" -> child_type)
        }

        fieldLogService.find(query, page = page, numberPerPage = numberPerPage) map {
          moduleArr => Ok(Json.toJson(moduleArr))
        }
      }
  }

  def count(field_ids: Option[List[String]], user_ids: Option[List[String]], child_id: Option[String], child_type: Option[String]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        var query = Json.obj()

        if (field_ids.get.nonEmpty) {
          query ++= Json.obj("field_id" -> Json.obj("$in" -> field_ids))
        }

        if (user_ids.get.nonEmpty) {
          query ++= Json.obj("user_id" -> Json.obj("$in" -> user_ids))
        }

        if (child_id.isDefined) {
          query ++= Json.obj("child_id" -> child_id)
        }

        if (child_type.isDefined) {
          query ++= Json.obj("child_type" -> child_type)
        }

        fieldLogService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        fieldLogService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          moduleArr => Ok(Json.toJson(moduleArr))
        }
      }
  }

}
