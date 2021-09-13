package controllers.mercure

import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import model.mercure.daos.status.{ Status => CStatus }
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.api.DB
import reactivemongo.bson.BSONDocument
import services.mercure.status.StatusService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by madalien on 23/05/16.
 */

class StatusCtrl @Inject() (
    val messagesApi: MessagesApi,
    val statusService: StatusService,
    application: play.api.Application,
    passwordHasher: PasswordHasher,
    authInfoRepository: AuthInfoRepository,

    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("MR." + this.getClass.getSimpleName);

  def createStatus = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[CStatus]) {
    implicit request =>
      {
        val status = request.body
        val status_id = UUID.randomUUID()
        statusService.insert(status.copy(id = Some(status_id))).map {
          res =>
            res.inError match {
              case true => {
                InternalServerError(Json.obj("res" -> s" ${res.message}  code: ${res.code}"))
              }
              case false => {
                Created(Json.obj("res" -> Json.obj("id" -> status_id)))
              }
            }
        }

      }
  }

  def updateStatus(id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[CStatus]) {
    implicit request =>
      {
        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        statusService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          case Some(oldSub: CStatus) => {
            Ok(Json.obj("res" -> "Status successfully updated"))
          }
          case _ => InternalServerError(Json.obj("res" -> "error while updating status"))
        }
      }
  }

  def statusList(page: Int, numberByPage: Int) = Action.async {
    implicit request =>
      {
        statusService.getList(page, numberByPage) map {
          statusArr => Ok(Json.toJson(statusArr))
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        statusService.remove(query).map {
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

  def getStatusByIds(ids: List[String]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        statusService.find(query).map {
          statusArr => Ok(Json.toJson(statusArr))
        }
      }
  }

  // refactoring in findone
  def findStatusById(id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> id)
        statusService.find(query).map {
          statusArr => Ok(Json.toJson(statusArr))
        }
      }
  }

  def count() = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        statusService.count(Json.obj()).map {
          nbUsr => Ok(Json.obj("res" -> nbUsr))
        }
      }
  }

}
