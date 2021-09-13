package controllers.cta

/**
 * Created by Clément Lelong O8/05/17
 */
import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import models.daos.faq.Faq
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.faq.FaqService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

class FaqCtrl @Inject() (
    val messagesApi: MessagesApi,
    val faqService: FaqService,
    application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);

  import models.daos.faq.FaqFormats._

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Faq]) {
    implicit request =>
      {
        val faq = request.body
        val uid = UUID.randomUUID()

        val docWithId = faq.copy(id = Some(uid), updated_date = Some(new DateTime()))
        faqService.insert(docWithId).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Faq]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        faqService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldFaq =>
            {
              mayOldFaq match {
                case Some(oldFaq: Faq) => {
                  Ok(Json.obj("res" -> "Faq successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating faq"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        faqService.remove(query).map {
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

        faqService.find(query, page = page, numberPerPage = numberPerPage) map {
          faqArr => Ok(Json.toJson(faqArr))
        }
      }
  }

  def count() = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = Json.obj()

        faqService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        faqService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          faqArr => Ok(Json.toJson(faqArr))
        }
      }
  }

}
