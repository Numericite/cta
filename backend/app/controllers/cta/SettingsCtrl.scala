package controllers.cta

/**
 * Created by madalien on 12/19/16.
 */

import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import models.daos.settings.Settings
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.settings.SettingsService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles
import play.api.libs.concurrent.Execution.Implicits._

class SettingsCtrl @Inject() (
    val messagesApi: MessagesApi,
    val settingsService: SettingsService,
    application: play.api.Application,
    passwordHasher: PasswordHasher,
    authInfoRepository: AuthInfoRepository,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {

  val logger = Logger("BP." + this.getClass.getSimpleName);

  import models.daos.settings.SettingsFormats._

  def create = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[Settings]) {
    implicit request =>
      {
        val uID = UUID.randomUUID()
        val Party = request.body.copy(id = Some(uID))
        settingsService.insert(Party).map {
          res =>
            res.inError match {
              case true => {
                InternalServerError(Json.obj("res" -> s" ${res.message}  code: ${res.code}"))
              }
              case false => {
                Created(Json.obj("res" -> Json.obj("id" -> uID.toString)))
              }
            }
        }
      }
  }

  def updateSettings(id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[Settings]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        settingsService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldSub =>
            {
              mayOldSub match {
                case Some(oldSub: Settings) => {
                  Ok(Json.obj("res" -> "Subject successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating subject"))
              }
            }
        }
      }
  }

  def settingsList(page: Int, numberByPage: Int) = Action.async {
    implicit request =>
      {
        settingsService.getList(page, numberByPage) map {
          attArr => Ok(Json.toJson(attArr))
        }
      }
  }

  def removeById(id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> id)
        settingsService.remove(query).map {
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

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        settingsService.remove(query).map {
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

  def getSettingsByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        settingsService.find(query).map {
          attArr => Ok(Json.toJson(attArr))
        }
      }
  }

  def findSettingsById(id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> id)
        settingsService.find(query).map {
          campArr => Ok(Json.toJson(campArr.headOption))
        }
      }
  }

  def count() = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        settingsService.count(BSONDocument()).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

}
