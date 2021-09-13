package controllers.cta

import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import models.daos.exploration.Exploration
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.exploration.ExplorationService
import services.mercure.user.UserService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

/**
 * Created by Clement LELONG on 13/06/19.
 */

class ExplorationCtrl @Inject() (
    val messagesApi: MessagesApi,
    val userService: UserService,
    val explorationService: ExplorationService,
    implicit val application: play.api.Application,
    passwordHasher: PasswordHasher,
    authInfoRepository: AuthInfoRepository,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("MR." + this.getClass.getSimpleName);

  import models.daos.exploration.ExplorationFormats._

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Exploration]) {
    implicit request =>
      {
        val exploration = request.body
        val uid = UUID.randomUUID()

        val docWithId = exploration.copy(id = Some(uid), created_date = Some(new DateTime()), updated_date = Some(new DateTime()))
        explorationService.insert(docWithId).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Exploration]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        explorationService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldExploration =>
            {
              mayOldExploration match {
                case Some(oldExploration: Exploration) => {
                  Ok(Json.obj("res" -> "Exploration successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating exploration"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        explorationService.remove(query).map {
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
    user_ids: Option[List[String]],
    exploration_type_ids: Option[List[String]]
  ) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        var query = BSONDocument()

        if (user_ids.get.nonEmpty) {
          query ++= BSONDocument("user_id" -> BSONDocument("$in" -> user_ids))
        }

        if (exploration_type_ids.get.nonEmpty) {
          query ++= BSONDocument("exploration_type_id" -> BSONDocument("$in" -> exploration_type_ids))
        }

        explorationService.find(query, page = page, numberPerPage = numberPerPage) map {
          explorationArr => Ok(Json.toJson(explorationArr))
        }
      }
  }

  def count(user_ids: Option[List[String]], exploration_type_ids: Option[List[String]]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        var query = Json.obj()

        if (user_ids.get.nonEmpty) {
          query ++= Json.obj("user_id" -> Json.obj("$in" -> user_ids))
        }

        if (exploration_type_ids.get.nonEmpty) {
          query ++= Json.obj("exploration_type_id" -> Json.obj("$in" -> exploration_type_ids))
        }

        explorationService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        explorationService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          explorationArr => Ok(Json.toJson(explorationArr))
        }
      }
  }

}
