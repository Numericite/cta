package controllers.cta

/**
 * Created by Clément Lelong O8/05/17
 */
import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import models.daos.version.Version
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.{ BSONArray, BSONDocument }
import services.version.VersionService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

class VersionCtrl @Inject() (
    val messagesApi: MessagesApi,
    val versionService: VersionService,
    application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);

  import models.daos.version.VersionFormats._

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Version]) {
    implicit request =>
      {
        val version = request.body
        val uid = UUID.randomUUID()

        val docWithId = version.copy(id = Some(uid), created_date = Some(new DateTime()))
        versionService.insert(docWithId).map {
          res =>
            res.inError match {
              case true => {
                InternalServerError(Json.obj("res" -> s" ${res.message}  code: ${res.code}"))
              }
              case false => {

                if (docWithId.isDefault) {
                  val othersQuery = BSONDocument(
                    "id" -> BSONDocument("$ne" -> uid.toString),
                    "parent_id" -> docWithId.parent_id,
                    "kind" -> docWithId.kind
                  )
                  val othersModifier = BSONDocument("$set" -> BSONDocument("isDefault" -> false))

                  versionService.update(othersQuery, othersModifier) map {
                    result => logger.debug(s"MODIFIED OTHER VERSIONS : ${result}")
                  }
                }

                Created(Json.obj("res" -> Json.obj("id" -> uid.toString)))
              }
            }
        }

      }
  }

  def update(id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Version]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        versionService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldVersion =>
            {
              mayOldVersion match {
                case Some(oldVersion: Version) => {

                  if (elemToUpdate.isDefault) {
                    val othersQuery = BSONDocument(
                      "id" -> BSONDocument("$ne" -> elemToUpdate.id.get.toString),
                      "parent_id" -> elemToUpdate.parent_id,
                      "kind" -> elemToUpdate.kind
                    )
                    val othersModifier = BSONDocument("$set" -> BSONDocument("isDefault" -> false))

                    versionService.update(othersQuery, othersModifier) map {
                      result => logger.debug(s"MODIFIED OTHER VERSIONS : ${result}")
                    }
                  }

                  Ok(Json.obj("res" -> "Version successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating version"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        versionService.remove(query).map {
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
    kind: String,
    parent_ids: Option[List[String]],
    isDefault: Option[Boolean]
  ) = Action.async {
    implicit request =>
      {
        var query = BSONDocument("kind" -> kind)

        if (parent_ids.get.nonEmpty) {
          query ++= BSONDocument("parent_id" -> BSONDocument("$in" -> parent_ids))
        }

        if (isDefault.isDefined) {
          query ++= BSONDocument("isDefault" -> isDefault)
        }

        versionService.find(query, page = page, numberPerPage = numberPerPage) map {
          versionArr =>
            {
              Ok(Json.toJson(versionArr))
            }
        }
      }
  }

  def count(kind: String, parent_ids: Option[List[String]], isDefault: Option[Boolean]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        var query = Json.obj("kind" -> kind)

        if (parent_ids.get.nonEmpty) {
          query ++= Json.obj("parent_id" -> Json.obj("$in" -> parent_ids))
        }

        if (isDefault.isDefined) {
          query ++= Json.obj("isDefault" -> isDefault)
        }

        versionService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getAvailableNames() = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val nameAggregateQuery = BSONArray(
          BSONDocument(
            "$match" -> BSONDocument(
              "kind" -> "file"
            )
          ),
          BSONDocument(
            "$group" -> BSONDocument(
              "_id" -> 0,
              "names" -> BSONDocument(
                "$addToSet" -> "$name"
              )
            )
          )
        )
        versionService.aggregate(nameAggregateQuery).map {
          result =>
            {
              Ok(((result \ "result")(0) \ "names").getOrElse(Json.arr()))
            }
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        versionService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          versionArr => Ok(Json.toJson(versionArr))
        }
      }
  }

}
