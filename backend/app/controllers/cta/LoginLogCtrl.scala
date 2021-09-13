package controllers.cta

/**
 * Created by Clément Lelong O8/05/17
 */
import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import models.daos.loginLog.LoginLog
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.loginLog.LoginLogService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

class LoginLogCtrl @Inject() (
    val messagesApi: MessagesApi,
    val loginLogService: LoginLogService,
    implicit val application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);

  import models.daos.loginLog.LoginLogFormats._

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[LoginLog]) {
    implicit request =>
      {
        val loginLog = request.body
        val uid = UUID.randomUUID()

        val docWithId = loginLog.copy(id = Some(uid), created_date = Some(new DateTime()))
        loginLogService.insert(docWithId).map {
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

  def count(user_ids: Option[List[String]]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        var query = Json.obj()

        if (user_ids.get.nonEmpty) {
          query ++= Json.obj("user_id" -> Json.obj("$in" -> user_ids))
        }

        loginLogService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getList(page: Int, numberPerPage: Int, user_ids: Option[List[String]]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        var query = BSONDocument()

        if (user_ids.get.nonEmpty) {
          query ++= BSONDocument("user_id" -> BSONDocument("$in" -> user_ids))
        }

        loginLogService.find(query, page = page, numberPerPage = numberPerPage) map {
          loginLogArr => Ok(Json.toJson(loginLogArr))
        }
      }
  }

  def removeByUsersIds(user_ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("user_id" -> BSONDocument("$in" -> user_ids))
        loginLogService.remove(query).map {
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

}
