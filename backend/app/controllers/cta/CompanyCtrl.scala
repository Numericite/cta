package controllers.cta

/**
 * Created by Clément Lelong O8/05/17
 */
import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import models.daos.company.Company
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.company.CompanyService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

class CompanyCtrl @Inject() (
    val messagesApi: MessagesApi,
    val companyService: CompanyService,
    implicit val application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);

  import models.daos.company.CompanyFormats._

  def create = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[Company]) {
    implicit request =>
      {
        val company = request.body
        val uid = UUID.randomUUID()

        val docWithId = company.copy(id = Some(uid), updated_date = Some(new DateTime()))
        companyService.insert(docWithId).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[Company]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        companyService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldCompany =>
            {
              mayOldCompany match {
                case Some(oldCompany: Company) => {
                  Ok(Json.obj("res" -> "Company successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating company"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        companyService.remove(query).map {
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
  ) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = BSONDocument()

        companyService.find(query, page = page, numberPerPage = numberPerPage) map {
          companyArr => Ok(Json.toJson(companyArr))
        }
      }
  }

  def count() = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = Json.obj()

        companyService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        companyService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          companyArr => Ok(Json.toJson(companyArr))
        }
      }
  }

}
