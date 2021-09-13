package controllers.cta

/**
 * Created by Clément Lelong O8/05/17
 */
import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import models.daos.tag.Tag
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.tag.TagService
import services.location.LocationService
import services.mercure.user.UserService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

import scala.concurrent.Future

class TagCtrl @Inject() (
    val messagesApi: MessagesApi,
    val tagService: TagService,
    application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);

  import models.daos.tag.TagFormats._

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Tag]) {
    implicit request =>
      {
        val tag = request.body
        val uid = UUID.randomUUID()

        val docWithId = tag.copy(id = Some(uid), updated_date = Some(new DateTime()))
        tagService.insert(docWithId).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Tag]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        tagService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldTag =>
            {
              mayOldTag match {
                case Some(oldTag: Tag) => {
                  Ok(Json.obj("res" -> "Tag successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating tag"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        tagService.remove(query).map {
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

        tagService.find(query, page = page, numberPerPage = numberPerPage) map {
          tagArr => Ok(Json.toJson(tagArr))
        }
      }
  }

  def count() = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = Json.obj()

        tagService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        tagService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          tagArr => Ok(Json.toJson(tagArr))
        }
      }
  }

}
