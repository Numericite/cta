package controllers.cta

import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import models.daos.restriction.Restriction
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.mercure.user.UserService
import services.restriction.RestrictionService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

/**
 * Created by Clement LELONG on 13/06/19.
 */

class RestrictionCtrl @Inject() (
    val messagesApi: MessagesApi,
    val userService: UserService,
    val restrictionService: RestrictionService,
    implicit val application: play.api.Application,
    passwordHasher: PasswordHasher,
    authInfoRepository: AuthInfoRepository,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("MR." + this.getClass.getSimpleName);

  import models.daos.restriction.RestrictionFormats._

  def create = silhouette.SecuredAction(WithRoles("NormalUser", "Partner")).async(parse.json[Restriction]) {
    implicit request =>
      {
        val restriction = request.body
        val uid = UUID.randomUUID()

        val docWithId = restriction.copy(id = Some(uid), created_date = Some(new DateTime()), updated_date = Some(new DateTime()))
        restrictionService.insert(docWithId).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("NormalUser", "Partner")).async(parse.json[Restriction]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        restrictionService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldRestriction =>
            {
              mayOldRestriction match {
                case Some(oldRestriction: Restriction) => {
                  Ok(Json.obj("res" -> "Restriction successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating restriction"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("NormalUser", "Partner")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        restrictionService.remove(query).map {
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
    exploration_type_id: Option[String]
  ) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        var query = BSONDocument()

        if (exploration_type_id.isDefined) {
          query ++= BSONDocument("exploration_type_id" -> exploration_type_id)
        }

        restrictionService.find(query, page = page, numberPerPage = numberPerPage) map {
          restrictionArr => Ok(Json.toJson(restrictionArr))
        }
      }
  }

  def count(exploration_type_id: Option[String]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        var query = Json.obj()

        if (exploration_type_id.isDefined) {
          query ++= Json.obj("exploration_type_id" -> exploration_type_id)
        }

        restrictionService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

}
