package controllers.cta

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.api.{ LoginInfo, Silhouette }
import com.typesafe.config.ConfigFactory
import fly.play.s3.{ BucketFile, S3, S3Exception }
import model.mercure.daos.user.User
import net.ceedubs.ficus.Ficus._
import org.apache.commons.io.FileUtils
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.{ BSONArray, BSONDocument }
import services.mercure.user.UserService
import services.school.SchoolService
import services.school.classroom.SchoolClassroomService
import utils.mercure.DefaultEnv
import utils.mercure.auth.{ IsOwner, WithRoles }

import scala.concurrent.Future

/**
 * Created by Clement LELONG on 13/06/19.
 */

class TeacherCtrl @Inject() (
    val messagesApi: MessagesApi,
    val userService: UserService,
    val schoolService: SchoolService,
    val classroomService: SchoolClassroomService,
    implicit val application: play.api.Application,
    passwordHasher: PasswordHasher,
    authInfoRepository: AuthInfoRepository,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("MR." + this.getClass.getSimpleName);

  import play.api.libs.json.Json
  import reactivemongo.play.json.BSONFormats._

  def count(school_ids: Option[List[String]], grade: Option[Int], classroom_id: Option[String]) = silhouette.SecuredAction(WithRoles("NormalUser", "Teacher") || WithRoles("NormalUser", "Operator")).async {
    implicit request =>
      {
        val queryUser = BSONDocument("config.accountType" -> "teacher")
        var queryClassroom = BSONDocument()
        var querySchool = BSONDocument()

        if (school_ids.get.nonEmpty) {
          querySchool ++= BSONDocument("config.school_id" -> BSONDocument("$in" -> school_ids))
        }

        if (grade.isDefined) {
          queryClassroom ++= BSONDocument("grade" -> grade)
        }

        if (classroom_id.isDefined) {
          queryClassroom ++= BSONDocument("id" -> classroom_id)
        }

        for {
          schools <- schoolService.find(querySchool)
          classrooms <- classroomService.find(queryClassroom ++ BSONDocument("school_id" -> BSONDocument("$in" -> schools.map(_.id.get.toString))))
          teachersCount <- userService.count(queryUser ++ BSONDocument("config.classroom_ids" -> BSONDocument("$in" -> classrooms.map(_.id.get.toString))))
        } yield {
          Ok(Json.obj("count" -> Json.toJson(teachersCount)))
        }
      }
  }

  def getTeachers(page: Int, numberPerPage: Int, school_ids: Option[List[String]], grade: Option[Int], classroom_id: Option[String]) = silhouette.SecuredAction(WithRoles("NormalUser", "Teacher") || WithRoles("NormalUser", "Operator")).async {
    implicit request =>
      {
        val queryUser = BSONDocument("config.accountType" -> "teacher")
        var queryClassroom = BSONDocument()
        var querySchool = BSONDocument()

        if (school_ids.get.nonEmpty) {
          querySchool ++= BSONDocument("id" -> BSONDocument("$in" -> school_ids))
        }

        if (grade.isDefined) {
          queryClassroom ++= BSONDocument("grade" -> grade)
        }

        if (classroom_id.isDefined) {
          queryClassroom ++= BSONDocument("id" -> classroom_id)
        }

        for {
          schools <- schoolService.find(querySchool)
          classrooms <- classroomService.find(queryClassroom ++ BSONDocument("school_id" -> BSONDocument("$in" -> schools.map(_.id.get.toString))))
          teachersArr <- userService.find(queryUser ++ BSONDocument("config.classroom_ids" -> BSONDocument("$in" -> classrooms.map(_.id.get.toString))), Json.obj("updated_date" -> -1), page, numberPerPage)
        } yield {
          Ok(Json.obj("res" -> Json.toJson(teachersArr)))
        }
      }
  }

  def getList(page: Int, numberPerPage: Int, school_ids: Option[List[String]]) = silhouette.SecuredAction(WithRoles("NormalUser", "Teacher") || WithRoles("NormalUser", "Operator")).async {
    implicit request =>
      {
        var query = BSONDocument("config.accountType" -> "teacher")

        if (school_ids.get.nonEmpty) {
          query ++= BSONDocument("config.school_id" -> BSONDocument("$in" -> school_ids))
        }

        userService.find(query, Json.obj("updated_date" -> -1), page, numberPerPage) map {
          userArr => Ok(Json.obj("res" -> Json.toJson(userArr)))
        }
      }
  }

  def countStudents(classroom_ids: List[String]) = silhouette.SecuredAction(WithRoles("NormalUser", "Teacher") || WithRoles("NormalUser", "Operator")).async {
    implicit request =>
      {
        val query = BSONDocument("config.classroom_id" -> BSONDocument("$in" -> classroom_ids), "config.accountType" -> "student")

        userService.count(query) map {
          userArr => Ok(Json.obj("count" -> Json.toJson(userArr)))
        }
      }
  }

  def getStudents(classroom_ids: List[String], page: Int, numberPerPage: Int) = silhouette.SecuredAction(WithRoles("NormalUser", "Teacher") || WithRoles("NormalUser", "Operator")).async {
    implicit request =>
      {
        val query = BSONDocument("config.classroom_id" -> BSONDocument("$in" -> classroom_ids), "config.accountType" -> "student")

        userService.find(query, Json.obj("updated_date" -> -1), page, numberPerPage) map {
          userArr => Ok(Json.obj("res" -> Json.toJson(userArr)))
        }
      }
  }

  def getStudentsByFullName(classroom_ids: List[String], fullName: String) = silhouette.SecuredAction(WithRoles("NormalUser", "Teacher") || WithRoles("NormalUser", "Operator")).async {
    implicit request =>
      {
        val aggregateQuery = BSONArray(
          BSONDocument(
            "$project" -> BSONDocument(
              "fullName" -> BSONDocument("$concat" -> BSONArray("$firstName", " ", "$lastName")),
              "firstName" -> true,
              "lastName" -> true,
              "avatar_path" -> true,
              "config" -> true
            )
          ),
          BSONDocument(
            "$match" -> BSONDocument(
              "fullName" -> BSONDocument("$regex" -> fullName, "$options" -> "i"),
              "config.classroom_id" -> BSONDocument("$in" -> classroom_ids)
            )
          )
        )

        userService.aggregate(aggregateQuery).map {
          result => Ok(result)
        }
      }
  }

  def searchByFullName(fullName: String) = silhouette.SecuredAction(WithRoles("NormalUser", "Teacher") || WithRoles("NormalUser", "Operator")).async {
    implicit request =>
      {
        val aggregateQuery = BSONArray(
          BSONDocument(
            "$project" -> BSONDocument(
              "fullName" -> BSONDocument("$concat" -> BSONArray("$firstName", " ", "$lastName")),
              "firstName" -> true,
              "lastName" -> true,
              "avatar_path" -> true,
              "config" -> true,
              "userID" -> true
            )
          ),
          BSONDocument(
            "$match" -> BSONDocument(
              "fullName" -> BSONDocument("$regex" -> fullName, "$options" -> "i"),
              "config.accountType" -> "teacher"
            )
          )
        )

        userService.aggregate(aggregateQuery).map {
          result => Ok(result)
        }
      }
  }

}
