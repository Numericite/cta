package controllers.cta

/**
 * Created by Clément Lelong O8/05/17
 */
import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import models.daos.school.report.SchoolReport
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.school.report.SchoolReportService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

class SchoolReportCtrl @Inject() (
    val messagesApi: MessagesApi,
    val schoolReportService: SchoolReportService,
    implicit val application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);

  import models.daos.school.report.SchoolReportFormats._

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[SchoolReport]) {
    implicit request =>
      {
        val schoolReport = request.body
        val uid = UUID.randomUUID()

        val docWithId = schoolReport.copy(id = Some(uid), updated_date = Some(new DateTime()), created_date = Some(new DateTime()))
        schoolReportService.insert(docWithId).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[SchoolReport]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        schoolReportService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldSchoolReport =>
            {
              mayOldSchoolReport match {
                case Some(oldSchoolReport: SchoolReport) => {
                  Ok(Json.obj("res" -> "SchoolReport successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating schoolReport"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        schoolReportService.remove(query).map {
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
    stream_ids: Option[List[String]]
  ) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        var query = BSONDocument()

        if (user_ids.get.nonEmpty) {
          query ++= BSONDocument("user_id" -> BSONDocument("$in" -> user_ids))
        }

        if (stream_ids.get.nonEmpty) {
          query ++= BSONDocument("stream_id" -> BSONDocument("$in" -> stream_ids))
        }

        schoolReportService.find(query, page = page, numberPerPage = numberPerPage) map {
          schoolReportArr => Ok(Json.toJson(schoolReportArr))
        }
      }
  }

  def count(user_ids: Option[List[String]], stream_ids: Option[List[String]]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        var query = Json.obj()

        if (user_ids.get.nonEmpty) {
          query ++= Json.obj("user_id" -> user_ids)
        }

        if (stream_ids.get.nonEmpty) {
          query ++= Json.obj("stream_id" -> stream_ids)
        }

        schoolReportService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        schoolReportService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          schoolReportArr => Ok(Json.toJson(schoolReportArr))
        }
      }
  }

}
