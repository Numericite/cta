package controllers.cta

/**
 * Created by Clément Lelong O8/05/17
 */
import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import models.daos.page.Page
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.page.PageService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

class PageCtrl @Inject() (
    val messagesApi: MessagesApi,
    val pageService: PageService,
    application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);

  import models.daos.page.PageFormats._

  def create = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[Page]) {
    implicit request =>
      {
        val page = request.body
        val uid = UUID.randomUUID()

        val docWithId = page.copy(id = Some(uid), status_name = "open", created_date = Some(new DateTime()))
        pageService.insert(docWithId).map {
          res =>
            res.inError match {
              case true => {
                InternalServerError(Json.obj("res" -> s" ${res.message}  code: ${res.code}"))
              }
              case false => {

                val othersQuery = BSONDocument(
                  "id" -> BSONDocument("$ne" -> uid.toString),
                  "version_id" -> docWithId.version_id,
                  "status_name" -> "open"
                )

                val othersModifier = BSONDocument("$set" -> BSONDocument("status_name" -> "closed"))
                pageService.update(othersQuery, othersModifier) map {
                  result => logger.debug(s"MODIFIED OTHER PAGES : ${result}")
                }

                Created(Json.obj("res" -> Json.obj("id" -> uid.toString)))
              }
            }
        }

      }
  }

  def update(id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[Page]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        pageService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldPage =>
            {
              mayOldPage match {
                case Some(oldPage: Page) => {
                  Ok(Json.obj("res" -> "Page successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating page"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        pageService.remove(query).map {
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
    numberPerPage: Int
  ) = Action.async {
    implicit request =>
      {
        val query = BSONDocument()

        pageService.find(query, page = page, numberPerPage = numberPerPage) map {
          pageArr => Ok(Json.toJson(pageArr))
        }
      }
  }

  def count() = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = Json.obj()

        pageService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        pageService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          pageArr => Ok(Json.toJson(pageArr))
        }
      }
  }

}
