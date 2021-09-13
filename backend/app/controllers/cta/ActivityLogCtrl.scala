package controllers.cta

/**
 * Created by Clément Lelong on 27/05/16.
 */

import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import models.daos.activity.log.ActivityLog
import models.daos.course.ActivityHelper
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.activity.log.ActivityLogService
import services.activity.ActivityService
import services.selection.SelectionService
import services.choice.ChoiceService
import services.course.CourseService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

import scala.concurrent.Future

class ActivityLogCtrl @Inject() (
    val messagesApi: MessagesApi,
    val activityLogService: ActivityLogService,
    val activityService: ActivityService,
    val choiceService: ChoiceService,
    val selectionService: SelectionService,
    val courseService: CourseService,
    implicit val application: play.api.Application,
    passwordHasher: PasswordHasher,
    authInfoRepository: AuthInfoRepository,

    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CTA." + this.getClass.getSimpleName);

  import models.daos.activity.log.ActivityLogFormats._
  import models.daos.selection.SelectionFormats._
  import models.daos.choice.ChoiceFormats._
  import models.daos.activity.ActivityFormats._

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[ActivityLog]) {
    implicit request =>
      {
        val uid = UUID.randomUUID()
        val activityLog = request.body.copy(id = Some(uid), updated_date = Some(new DateTime()), created_date = Some(new DateTime()))
        activityLogService.insert(activityLog).map {
          res =>
            res.inError match {
              case true => {
                InternalServerError(Json.obj("res" -> s" ${res.message}  code: ${res.code}"))
              }
              case false => {
                val othersQuery = BSONDocument(
                  "activity_id" -> activityLog.activity_id,
                  "user_id" -> activityLog.user_id,
                  "id" -> BSONDocument("$ne" -> uid.toString),
                  "status_name" -> "open"
                )
                val othersModifier = BSONDocument("$set" -> BSONDocument("status_name" -> "closed"))
                activityLogService.update(othersQuery, othersModifier) map {
                  result => logger.debug(s"MODIFIED : ${result}")
                }
                Created(Json.obj("res" -> Json.obj("id" -> uid.toString)))
              }
            }
        }
      }
  }

  def updateStatus(id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json) {

    implicit request =>
      {
        (request.body \ "status_name").validate[String].fold(
          error => { Future.successful(BadRequest(s"error")) },
          status_name => {

            var fieldsToUpdate = BSONDocument(
              "status_name" -> status_name
            )

            val subModifier = BSONDocument(
              "$set" -> fieldsToUpdate
            )

            val selec = BSONDocument("id" -> id)
            activityLogService.update(selec, subModifier) map {
              res =>
                res.inError match {
                  case true => {
                    InternalServerError(Json.obj("res" -> s" ${res.message}  code: ${res.code}"))
                  }
                  case false => {
                    Ok(Json.obj("res" -> s" ${res.message} updated Done"))
                  }
                }
            }
          }
        )

      }
  }

  def getByUserId(user_id: String, activity_id: Option[String]) = Action.async {
    implicit request =>
      {
        var query = BSONDocument("user_id" -> user_id, "status_name" -> "open")

        if (activity_id.isDefined) {
          query ++= BSONDocument("activity_id" -> activity_id)
        }

        activityLogService.find(query, page = 1, numberPerPage = 1000) map {
          activityLogArr => Ok(Json.toJson(activityLogArr))
        }
      }
  }

  def getUserAdvancedLogs(user_id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = BSONDocument("user_id" -> user_id, "status_name" -> "open")

        activityLogService.find(query, page = 1, numberPerPage = 1000) flatMap {
          activityLogArr =>
            {
              val selection_ids = activityLogArr flatten (_.selection_ids.getOrElse(List()))
              val selectionQuery = BSONDocument("id" -> BSONDocument("$in" -> selection_ids))

              val choice_ids = activityLogArr flatten (_.choice_ids.getOrElse(List()))
              val choiceQuery = BSONDocument("id" -> BSONDocument("$in" -> choice_ids))

              val getSelections = selectionService.find(selectionQuery, page = 1, numberPerPage = 1000) map { selections => selections }
              val getChoices = choiceService.find(choiceQuery, page = 1, numberPerPage = 1000) map { choices => choices }

              for {
                selectionArr <- getSelections
                choiceArr <- getChoices
              } yield {
                val response = Json.obj(
                  "logs" -> Json.toJson(activityLogArr),
                  "choices" -> Json.toJson(choiceArr),
                  "selections" -> Json.toJson(selectionArr)
                )
                Ok(response)
              }
            }
        }
      }
  }

  def getUserHistory(user_id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = BSONDocument("user_id" -> user_id, "status_name" -> "open", "activity_id" -> BSONDocument("$ne" -> "final"))
        activityLogService.find(query, page = 1, numberPerPage = 1000) flatMap {
          activityLogArr =>
            {
              val activity_ids = activityLogArr map (_.activity_id)
              val activityQuery = BSONDocument(
                "id" -> BSONDocument(
                  "$in" -> activity_ids
                )
              )
              activityService.find(activityQuery, page = 1, numberPerPage = 1000) map {
                activityArr =>
                  {
                    val historyArray = activityLogArr map (log => Json.obj("activity_name" -> activityArr.find(_.id.get.toString == log.activity_id).get.name, "created_date" -> log.created_date))
                    Ok(Json.toJson(historyArray))
                  }
              }
            }
        }
      }
  }

  def getUserUndoneActivities(user_id: String, course_id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val logQuery = BSONDocument("user_id" -> user_id, "status_name" -> "open", "activity_id" -> BSONDocument("$ne" -> "final"))
        val courseQuery = BSONDocument("id" -> course_id)

        val logRequest = activityLogService.find(logQuery, page = 1, numberPerPage = 1000) map { logs => logs }
        val courseRequest = courseService.find(courseQuery, page = 1, numberPerPage = 1000) map { courses => courses(0) }

        for {
          activityLogArr <- logRequest
          course <- courseRequest
          activityArr <- activityService.find(BSONDocument(
            "id" -> BSONDocument(
              "$nin" -> activityLogArr.map(_.activity_id),
              "$in" -> course.chapters.find(_.slug == "activity-area").get.children.find(_.slug == "introspection").get.activities.getOrElse(List()).map(_.id)
            )
          ), page = 1, numberPerPage = 1000)
        } yield {
          Ok(Json.toJson(activityArr))
        }

      }
  }

}
