package controllers.cta

/**
 * Created by Clément Lelong O8/05/17
 */
import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import models.daos.onisep.feedback.OnisepFeedback
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.onisep.feedback.OnisepFeedbackService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

class OnisepFeedbackCtrl @Inject() (
    val messagesApi: MessagesApi,
    val onisepFeedbackService: OnisepFeedbackService,
    application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);

  import models.daos.onisep.feedback.OnisepFeedbackFormats._

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[OnisepFeedback]) {
    implicit request =>
      {
        val onisepFeedback = request.body
        val uid = UUID.randomUUID()

        val docWithId = onisepFeedback.copy(id = Some(uid), updated_date = Some(new DateTime()))
        onisepFeedbackService.insert(docWithId).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[OnisepFeedback]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        onisepFeedbackService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldOnisepFeedback =>
            {
              mayOldOnisepFeedback match {
                case Some(oldOnisepFeedback: OnisepFeedback) => {
                  Ok(Json.obj("res" -> "OnisepFeedback successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating onisepFeedback"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        onisepFeedbackService.remove(query).map {
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

  def removeByCodeUais(code_uais: List[String], user_id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = BSONDocument("code_uai" -> BSONDocument("$in" -> code_uais), "user_id" -> user_id)
        onisepFeedbackService.remove(query).map {
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
    user_ids: Option[List[String]]
  ) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        var query = BSONDocument()

        if (user_ids.get.nonEmpty) {
          query ++= BSONDocument("user_id" -> BSONDocument("$in" -> user_ids))
        }

        onisepFeedbackService.find(query, page = page, numberPerPage = numberPerPage) map {
          onisepFeedbackArr => Ok(Json.toJson(onisepFeedbackArr))
        }
      }
  }

  def count(
    user_ids: Option[List[String]]
  ) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        var query = Json.obj()

        if (user_ids.get.nonEmpty) {
          query ++= Json.obj("user_id" -> Json.obj("$in" -> user_ids))
        }

        onisepFeedbackService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        onisepFeedbackService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          onisepFeedbackArr => Ok(Json.toJson(onisepFeedbackArr))
        }
      }
  }

}
