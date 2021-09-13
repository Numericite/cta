package controllers.cta

/**
 * Created by Clément Lelong O8/05/17
 */
import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import models.daos.selector.Selector
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.selector.SelectorService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

class SelectorCtrl @Inject() (
    val messagesApi: MessagesApi,
    val selectorService: SelectorService,
    application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);

  import models.daos.selector.SelectorFormats._

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Selector]) {
    implicit request =>
      {
        val selector = request.body
        val uid = UUID.randomUUID()

        val docWithId = selector.copy(id = Some(uid), updated_date = Some(selector.updated_date.getOrElse(new DateTime())))
        selectorService.insert(docWithId).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Selector]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        selectorService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldSelector =>
            {
              mayOldSelector match {
                case Some(oldSelector: Selector) => {
                  Ok(Json.obj("res" -> "Selector successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating selector"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        selectorService.remove(query).map {
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
    selection_ids: Option[List[String]]
  ) = Action.async {
    implicit request =>
      {
        var query = BSONDocument()

        if (selection_ids.get.nonEmpty) {
          query ++= BSONDocument("selection_id" -> BSONDocument("$in" -> selection_ids))
        }

        selectorService.find(query, page = page, numberPerPage = numberPerPage) map {
          selectorArr => Ok(Json.toJson(selectorArr))
        }
      }
  }

  def count(selection_ids: Option[List[String]]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        var query = Json.obj()

        if (selection_ids.get.nonEmpty) {
          query ++= Json.obj("selection_id" -> Json.obj("$in" -> selection_ids))
        }

        selectorService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        selectorService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          selectorArr => Ok(Json.toJson(selectorArr))
        }
      }
  }

}
