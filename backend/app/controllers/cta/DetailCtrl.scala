package controllers.cta

/**
 * Created by Clément Lelong O8/05/17
 */
import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.typesafe.config.ConfigFactory
import fly.play.s3.{ BucketFile, S3, S3Exception }
import models.daos.detail.Detail
import net.ceedubs.ficus.Ficus._
import org.apache.commons.io.FileUtils
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.detail.DetailService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

import scala.concurrent.Future

class DetailCtrl @Inject() (
    val messagesApi: MessagesApi,
    val detailService: DetailService,
    implicit val application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);

  import models.daos.detail.DetailFormats._

  def create = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[Detail]) {
    implicit request =>
      {
        val detail = request.body
        val uid = UUID.randomUUID()

        val docWithId = detail.copy(id = Some(uid), updated_date = Some(new DateTime()))
        detailService.insert(docWithId).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[Detail]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val detail = BSONDocument("id" -> id);
        detailService.findAndModify(detail, elemToUpdate, BSONDocument()) map {
          mayOldDetail =>
            {
              mayOldDetail match {
                case Some(oldDetail: Detail) => {
                  Ok(Json.obj("res" -> "Detail successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating detail"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        detailService.remove(query).map {
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
    domain_ids: Option[List[String]]
  ) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        var query = BSONDocument()

        if (domain_ids.get.nonEmpty) {
          query ++= BSONDocument("domain_id" -> BSONDocument("$in" -> domain_ids))
        }

        detailService.find(query, page = page, numberPerPage = numberPerPage) map {
          detailArr => Ok(Json.toJson(detailArr))
        }
      }
  }

  def count(domain_ids: Option[List[String]]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        var query = Json.obj()

        if (domain_ids.get.nonEmpty) {
          query ++= Json.obj("domain_id" -> Json.obj("$in" -> domain_ids))
        }

        detailService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        detailService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          detailArr => Ok(Json.toJson(detailArr))
        }
      }
  }
}
