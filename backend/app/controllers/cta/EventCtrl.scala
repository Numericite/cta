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
import models.daos.event.Event
import models.daos.booking.Booking
import org.apache.commons.io.FileUtils
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.event.EventService
import services.booking.BookingService
import services.mercure.user.UserService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

import scala.concurrent.Future

class EventCtrl @Inject() (
    val messagesApi: MessagesApi,
    val eventService: EventService,
    val bookingService: BookingService,
    val userService: UserService,
    implicit val application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);

  import models.daos.event.EventFormats._

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Event]) {
    implicit request =>
      {
        val Event = request.body
        val uid = UUID.randomUUID()

        val docWithId = Event.copy(id = Some(uid), created_date = Some(new DateTime()), updated_date = Some(new DateTime()))
        eventService.insert(docWithId).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Event]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        eventService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldEvent =>
            {
              mayOldEvent match {
                case Some(oldEvent: Event) => {
                  Ok(Json.obj("res" -> "Event successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating Event"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        eventService.remove(query).map {
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

  def getList(page: Int, numberPerPage: Int, kind: Option[String], ids: Option[List[String]]) = Action.async {
    implicit request =>
      {
        var query = BSONDocument()

        if (kind.nonEmpty) {
          query ++= BSONDocument("kind" -> kind)
        }

        if (ids.get.nonEmpty) {
          query ++= BSONDocument("id" -> BSONDocument("$in" -> ids))
        }

        eventService.find(query, page = page, numberPerPage = numberPerPage) map {
          EventArr => Ok(Json.toJson(EventArr))
        }
      }
  }

  def getByUserIds(user_ids: List[String], kind: String) = Action.async {
    implicit request =>
      {

        val query = BSONDocument("user_id" -> BSONDocument("$in" -> user_ids))

        for {
          eventBookingUser <- bookingService.find(query, page = 1, numberPerPage = 10000)
          events <- eventService.find(BSONDocument("id" -> BSONDocument("$in" -> eventBookingUser.map(item => item.parent_id)), "kind" -> kind), page = 1, numberPerPage = 10000)
          eventBookings <- bookingService.find(BSONDocument("parent_id" -> BSONDocument("$in" -> events.map(item => item.id.get.toString))), page = 1, numberPerPage = 10000)
          users <- userService.find(BSONDocument("userID" -> BSONDocument("$in" -> eventBookings.map(item => item.user_id))), page = 1, numberPerPage = 10000)
        } yield {

          var eventsJson = Json.arr()
          events.foreach(event => {
            if (eventBookings.map(_.parent_id).contains(event.id.get.toString)) {
              var eventBookingsJson = Json.arr()

              eventBookings.foreach(eventBooking => {
                if (eventBooking.parent_id == event.id.get.toString) {
                  eventBookingsJson = eventBookingsJson :+ Json.obj(
                    "id" -> eventBooking.id,
                    "parent_id" -> eventBooking.parent_id,
                    "user_id" -> eventBooking.user_id,
                    "status" -> eventBooking.status,
                    "user" -> users.find(user => user.userID.get.toString == eventBooking.user_id),
                    "created_date" -> eventBooking.created_date,
                    "updated_date" -> eventBooking.updated_date
                  )
                }
              })

              eventsJson = eventsJson :+ Json.obj(
                "id" -> event.id,
                "kind" -> event.kind,
                "kind_object" -> event.kind_object,
                "status" -> event.status,
                "title" -> event.title,
                "description" -> event.description,
                "startDate" -> event.startDate,
                "hours" -> event.hours,
                "endDate" -> event.endDate,
                "bookings" -> eventBookingsJson,
                "created_date" -> event.created_date,
                "updated_date" -> event.updated_date
              )
            }
          })

          Ok(Json.toJson(eventsJson))
        }

      }
  }

  def count(kind: Option[String], ids: Option[List[String]]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        var query = Json.obj()

        if (kind.nonEmpty) {
          query ++= Json.obj("kind" -> kind)
        }

        if (ids.get.nonEmpty) {
          query ++= Json.obj("id" -> Json.obj("$in" -> ids))
        }

        eventService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        eventService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          EventArr => Ok(Json.toJson(EventArr))
        }
      }
  }

}