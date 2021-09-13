package controllers.cta

/**
 * Created by Clément Lelong O8/05/17
 */
import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import models.daos.school.classroom.SchoolClassroom
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.school.classroom.SchoolClassroomService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

class SchoolClassroomCtrl @Inject() (
    val messagesApi: MessagesApi,
    val schoolClassroomService: SchoolClassroomService,
    implicit val application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);

  import models.daos.school.classroom.SchoolClassroomFormats._

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[SchoolClassroom]) {
    implicit request =>
      {
        val schoolClassroom = request.body
        val uid = UUID.randomUUID()

        val docWithId = schoolClassroom.copy(id = Some(uid), updated_date = Some(new DateTime()))
        schoolClassroomService.insert(docWithId).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[SchoolClassroom]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        schoolClassroomService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldSchoolClassroom =>
            {
              mayOldSchoolClassroom match {
                case Some(oldSchoolClassroom: SchoolClassroom) => {
                  Ok(Json.obj("res" -> "SchoolClassroom successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating schoolClassroom"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        schoolClassroomService.remove(query).map {
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
    school_ids: Option[List[String]],
    grade: Option[Int]
  ) = Action.async {
    implicit request =>
      {
        var query = BSONDocument()

        if (school_ids.get.nonEmpty) {
          query ++= BSONDocument("school_id" -> BSONDocument("$in" -> school_ids))
        }

        if (grade.isDefined) {
          query ++= BSONDocument("grade" -> grade)
        }

        schoolClassroomService.find(query, page = page, numberPerPage = numberPerPage) map {
          schoolClassroomArr => Ok(Json.toJson(schoolClassroomArr))
        }
      }
  }

  def count(school_ids: Option[List[String]], grade: Option[Int]) = Action.async {
    implicit request =>
      {
        var query = Json.obj()

        if (school_ids.get.nonEmpty) {
          query ++= Json.obj("school_id" -> Json.obj("$in" -> school_ids))
        }

        if (grade.isDefined) {
          query ++= Json.obj("grade" -> grade)
        }

        schoolClassroomService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        schoolClassroomService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          schoolClassroomArr => Ok(Json.toJson(schoolClassroomArr))
        }
      }
  }

}
