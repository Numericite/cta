package controllers.cta

/**
 * Created by Clément Lelong O8/05/17
 */
import java.util.UUID
import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.typesafe.config.ConfigFactory
import fly.play.s3.{ BucketFile, S3, S3Exception }
import net.ceedubs.ficus.Ficus._
import models.daos.booking.Booking
import org.apache.commons.io.FileUtils
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.booking.BookingService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

import scala.concurrent.Future

class BookingCtrl @Inject() (
    val messagesApi: MessagesApi,
    val BookingService: BookingService,
    implicit val application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);

  import models.daos.booking.BookingFormats._

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Booking]) {
    implicit request =>
      {
        val Booking = request.body
        val uid = UUID.randomUUID()

        val docWithId = Booking.copy(id = Some(uid), created_date = Some(new DateTime()), updated_date = Some(new DateTime()))
        BookingService.insert(docWithId).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Booking]) {

    implicit request =>
      {
        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        BookingService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldBooking =>
            {
              mayOldBooking match {
                case Some(oldBooking: Booking) => {
                  Ok(Json.obj("res" -> "Booking successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating Booking"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        BookingService.remove(query).map {
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

  def removeByParentIds(parent_ids: List[String]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = BSONDocument("parent_id" -> BSONDocument("$in" -> parent_ids))
        BookingService.remove(query).map {
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

  def getList(page: Int, numberPerPage: Int, user_ids: Option[List[String]], parent_ids: Option[List[String]], status: Option[String]) = Action.async {
    implicit request =>
      {
        var query = BSONDocument()

        if (user_ids.get.nonEmpty) {
          query ++= BSONDocument("user_id" -> BSONDocument("$in" -> user_ids))
        }

        if (parent_ids.get.nonEmpty) {
          query ++= BSONDocument("parent_id" -> BSONDocument("$in" -> parent_ids))
        }

        if (status.isDefined) {
          query ++= BSONDocument("status" -> status)
        }

        BookingService.find(query, page = page, numberPerPage = numberPerPage) map {
          BookingArr => Ok(Json.toJson(BookingArr))
        }
      }
  }

  def count(user_ids: Option[List[String]], parent_ids: Option[List[String]], status: Option[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        var query = Json.obj()

        if (user_ids.get.nonEmpty) {
          query ++= Json.obj("user_id" -> Json.obj("$in" -> user_ids))
        }

        if (parent_ids.get.nonEmpty) {
          query ++= Json.obj("parent_id" -> Json.obj("$in" -> parent_ids))
        }

        if (status.isDefined) {
          query ++= Json.obj("status" -> status)
        }

        BookingService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        BookingService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          BookingArr => Ok(Json.toJson(BookingArr))
        }
      }
  }

}