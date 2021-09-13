package controllers.cta

/**
 * Created by Clément Lelong O8/05/17
 */
import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import models.daos.course.Course
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.course.CourseService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

class CourseCtrl @Inject() (
    val messagesApi: MessagesApi,
    val courseService: CourseService,
    implicit val application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);

  import models.daos.course.CourseFormats._

  def create = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[Course]) {
    implicit request =>
      {
        val course = request.body
        val uid = UUID.randomUUID()

        val docWithId = course.copy(id = Some(uid), created_date = Some(new DateTime()))
        courseService.insert(docWithId).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[Course]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val course = BSONDocument("id" -> id);
        courseService.findAndModify(course, elemToUpdate, BSONDocument()) map {
          mayOldCourse =>
            {
              mayOldCourse match {
                case Some(oldCourse: Course) => {
                  Ok(Json.obj("res" -> "Course successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating course"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        courseService.remove(query).map {
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

        courseService.find(query, page = page, numberPerPage = numberPerPage) map {
          courseArr => Ok(Json.toJson(courseArr))
        }
      }
  }

  def count() = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = Json.obj()

        courseService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        courseService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          courseArr => Ok(Json.toJson(courseArr))
        }
      }
  }
}
