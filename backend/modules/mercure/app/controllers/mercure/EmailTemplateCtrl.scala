package controllers.mercure

import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import model.mercure.daos.email.Template
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.mercure.email.TemplateService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by madalien on 23/05/16.
 */

class EmailTemplateCtrl @Inject() (
    val messagesApi: MessagesApi,
    val templateService: TemplateService,
    application: play.api.Application,
    passwordHasher: PasswordHasher,
    authInfoRepository: AuthInfoRepository,

    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("MR." + this.getClass.getSimpleName);

  def create = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[Template]) {
    implicit request =>
      {
        val template = request.body
        val template_id = UUID.randomUUID()
        templateService.insert(template.copy(id = Some(template_id))).map {
          res =>
            res.inError match {
              case true => {
                InternalServerError(Json.obj("res" -> s" ${res.message}  code: ${res.code}"))
              }
              case false => {
                Created(Json.obj("res" -> Json.obj("id" -> template_id)))
              }
            }
        }

      }
  }

  def update(id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[Template]) {
    implicit request =>
      {
        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        templateService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          case Some(oldSub: Template) => {
            Ok(Json.obj("res" -> "Template successfully updated"))
          }
          case _ => InternalServerError(Json.obj("res" -> "error while updating template"))
        }
      }
  }

  def list(page: Int, numberByPage: Int) = Action.async {
    implicit request =>
      {
        templateService.getList(page, numberByPage) map {
          templateArr => Ok(Json.toJson(templateArr))
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        templateService.remove(query).map {
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

  def getByIds(ids: List[String]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        templateService.find(query).map {
          templateArr => Ok(Json.toJson(templateArr))
        }
      }
  }

  // refactoring in findone
  def getById(id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> id)
        templateService.find(query).map {
          templateArr => Ok(Json.toJson(templateArr))
        }
      }
  }

  def count() = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        templateService.count(Json.obj()).map {
          nbUsr => Ok(Json.obj("res" -> nbUsr))
        }
      }
  }

}
