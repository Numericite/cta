package controllers.cta

import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import models.daos.field.Field
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.mercure.user.UserService
import services.field.FieldService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

/**
 * Created by Clement LELONG on 13/06/19.
 */

class FieldCtrl @Inject() (
    val messagesApi: MessagesApi,
    val userService: UserService,
    val fieldService: FieldService,
    implicit val application: play.api.Application,
    passwordHasher: PasswordHasher,
    authInfoRepository: AuthInfoRepository,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("MR." + this.getClass.getSimpleName);

  import models.daos.field.FieldFormats._

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Field]) {
    implicit request =>
      {
        val field = request.body
        val uid = UUID.randomUUID()

        val docWithId = field.copy(id = Some(uid), created_date = Some(new DateTime()), updated_date = Some(new DateTime()))
        fieldService.insert(docWithId).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Field]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        fieldService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldField =>
            {
              mayOldField match {
                case Some(oldField: Field) => {
                  Ok(Json.obj("res" -> "Module successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating module"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("NormalUser", "Partner")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        fieldService.remove(query).map {
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
    parent_id: Option[String]
  ) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        var query = BSONDocument()

        if (parent_id.nonEmpty) {
          query ++= BSONDocument("parent_id" -> parent_id)
        }

        fieldService.find(query, page = page, numberPerPage = numberPerPage) map {
          moduleArr => Ok(Json.toJson(moduleArr))
        }
      }
  }

  def count(parent_id: Option[String]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        var query = Json.obj()

        if (parent_id.nonEmpty) {
          query ++= Json.obj("parent_id" -> parent_id)
        }

        fieldService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        fieldService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          moduleArr => Ok(Json.toJson(moduleArr))
        }
      }
  }

}
