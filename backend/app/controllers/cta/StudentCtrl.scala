package controllers.cta

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import org.joda.time.{ DateTime, Duration }
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.Controller
import reactivemongo.bson.{ BSONArray, BSONDocument }
import services.exploration.ExplorationService
import services.explorationType.ExplorationTypeService
import services.field.FieldService
import services.fieldlog.FieldLogService
import services.lead.LeadService
import services.school.SchoolService
import services.school.classroom.SchoolClassroomService
import services.mercure.user.UserService
import services.module.ModuleService
import services.module.activity.ModuleActivityService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

/**
 * Created by Clement LELONG on 13/06/19.
 */

class StudentCtrl @Inject() (
    val messagesApi: MessagesApi,
    val userService: UserService,
    val moduleService: ModuleService,
    val moduleActivityService: ModuleActivityService,
    val fieldLogService: FieldLogService,
    val fieldService: FieldService,
    val explorationService: ExplorationService,
    val explorationTypeService: ExplorationTypeService,
    val leadService: LeadService,
    val schoolService: SchoolService,
    val classroomService: SchoolClassroomService,
    implicit val application: play.api.Application,
    passwordHasher: PasswordHasher,
    authInfoRepository: AuthInfoRepository,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("MR." + this.getClass.getSimpleName);

  import play.api.libs.json.Json
  import models.daos.explorationType.ExplorationTypeFormats._
  import models.daos.module.ModuleFormats._
  import models.daos.lead.LeadFormats._
  import models.daos.field.FieldFormats._
  import models.daos.fieldlog.FieldLogFormats._
  import models.daos.exploration.ExplorationFormats._
  import models.daos.module.activity.ModuleActivityFormats._

  def getNextActions(user_id: String, grade: Option[Int], school_type: Option[String]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val fieldLogsQuery = BSONDocument("user_id" -> user_id)
        val explorationsQuery = BSONDocument("user_id" -> user_id)
        val leadsQuery = BSONDocument("user_id" -> user_id, "choice_ease" -> BSONDocument("$exists" -> false))
        var modulesQuery = BSONDocument()

        if (grade.isDefined) {
          modulesQuery ++= BSONDocument("grade" -> grade)
        }

        if (school_type.isDefined) {
          modulesQuery ++= BSONDocument("school_type" -> school_type)
        }

        val fieldLogsRequest = fieldLogService.find(fieldLogsQuery, page = 1, numberPerPage = 1000)

        for {
          logs <- fieldLogsRequest
          fields <- fieldService.find(BSONDocument("id" -> BSONDocument("$in" -> logs.map(_.field_id))), page = 1, numberPerPage = 1000)
          explorations <- explorationService.find(explorationsQuery, page = 1, numberPerPage = 1000)
          explorationTypes <- explorationTypeService.find(BSONDocument(), page = 1, numberPerPage = 1000)
          modules <- moduleService.find(modulesQuery, page = 1, numberPerPage = 1000)
          leads <- leadService.find(leadsQuery, page = 1, numberPerPage = 1000)
          activities <- moduleActivityService.find(BSONDocument("id" -> fields.map(_.parent_id)))
        } yield {

          var nextActions = Json.obj(
            "nextLeads" -> Json.toJson(leads)
          )

          var nextModule = modules.find(_.num == (modules.map(_.num).min))

          val modulesDone = modules.filter(module => (fields.map(_.parent_id).contains(module.id.get.toString) || logs.filter(_.field_id == "fake_log").map(_.child_id).contains(module.id.get.toString)))

          if (modulesDone.length > 0) {
            nextModule = modules.find(_.num == (modulesDone.map(_.num).max + 1))
            val lastModule = modules.find(_.num == modulesDone.map(_.num).max)
            if (lastModule.isDefined) {

              val lastModuleFieldIds = fields.filter(_.parent_id == lastModule.get.id.get.toString).map(_.id.get.toString)
              val lastModuleDate = logs.filter(log => lastModuleFieldIds.contains(log.field_id) || logs.filter(_.field_id == "fake_log").map(_.child_id).contains(lastModule.get.id.get.toString))(0).created_date

              val today = new DateTime()
              val explorationsToComplete = explorations.filter(
                exploration => (new Duration(exploration.date.getOrElse(new DateTime()), today).getStandardMinutes() > 0) && (!logs.filter(_.child_type == "exploration").map(_.child_id).contains(exploration.id.get.toString))
              )

              val explorationTypesDone = explorationTypes.filter(eType => explorations.filter(_.created_date.get.isAfter(lastModuleDate.get)).map(_.exploration_type_id).contains(eType.id.get.toString))
              val nextExplorationTypes = explorationTypes.filter(explorationType =>
                lastModule.get.exploration_type_ids.getOrElse(List())
                  .filter(exploration_type_id => !(explorationTypesDone.map(_.id.get.toString).contains(exploration_type_id))).contains(explorationType.id.get.toString))
              val nextExplorationTypesToComplete = explorationTypes.filter(explorationType => explorationsToComplete.map(_.exploration_type_id).contains(explorationType.id.get.toString))

              nextActions ++= Json.obj("nextExplorationTypes" -> Json.toJson(nextExplorationTypes))
              nextActions ++= Json.obj("nextExplorationTypesToComplete" -> Json.toJson(nextExplorationTypesToComplete))
              nextActions ++= Json.obj("explorationsToComplete" -> Json.toJson(explorationsToComplete))
            }
          }

          if (nextModule.isDefined) {
            nextActions ++= Json.obj("nextModule" -> Json.toJson(nextModule))
          }

          Ok(nextActions)
        }
      }
  }

  def getResults(user_id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val fieldLogsQuery = BSONDocument("user_id" -> user_id)
        val leadsQuery = BSONDocument("user_id" -> user_id, "choice_ease" -> BSONDocument("$exists" -> true))

        for {
          logs <- fieldLogService.find(fieldLogsQuery, page = 1, numberPerPage = 1000)
          fields <- fieldService.find(BSONDocument("id" -> BSONDocument("$in" -> logs.map(_.field_id))), page = 1, numberPerPage = 1000)
          modules <- moduleService.find(BSONDocument("id" -> BSONDocument("$in" -> fields.map(_.parent_id))), page = 1, numberPerPage = 1000)
          activities <- moduleActivityService.find(BSONDocument("id" -> BSONDocument("$in" -> fields.map(_.parent_id))), page = 1, numberPerPage = 1000)
          explorations <- explorationService.find(BSONDocument("user_id" -> user_id, "id" -> BSONDocument("$in" -> logs.filter(_.child_type == "exploration").map(_.child_id))), page = 1, numberPerPage = 1000)
          explorationTypes <- explorationTypeService.find(BSONDocument("id" -> BSONDocument("$in" -> explorations.map(_.exploration_type_id))), page = 1, numberPerPage = 1000)
          leads <- leadService.find(leadsQuery, page = 1, numberPerPage = 1000)
        } yield {
          var results = Json.arr()

          modules.foreach(
            module => {
              val moduleFields = fields.filter(module.id.get.toString == _.parent_id)
              val moduleLogs = logs.filter(log => moduleFields.map(_.id.get.toString).contains(log.field_id))

              var moduleObj = Json.obj(
                "kind" -> "module",
                "entity" -> Json.toJson(module),
                "fields" -> Json.toJson(moduleFields),
                "logs" -> Json.toJson(moduleLogs),
                "date" -> moduleLogs.map(_.created_date.get.getMillis()).max
              )

              var childrenArr = Json.arr()

              activities.filter(_.module_id == module.id.get.toString).foreach(
                activity => {
                  val activityFields = fields.filter(activity.id.get.toString == _.parent_id)
                  val activityLogs = logs.filter(log => activityFields.map(_.id.get.toString).contains(log.field_id))

                  val activityObj = Json.obj(
                    "kind" -> "activity",
                    "entity" -> Json.toJson(activity),
                    "fields" -> Json.toJson(activityFields),
                    "logs" -> Json.toJson(activityLogs),
                    "date" -> activityLogs.map(_.created_date.get.getMillis()).max
                  )

                  childrenArr = childrenArr.+:(activityObj)
                }
              )

              moduleObj ++= Json.obj("children" -> childrenArr)

              results = results.+:(moduleObj)
            }
          )

          explorations.foreach(
            exploration => {
              val explorationType = explorationTypes.find(_.id.get.toString == exploration.exploration_type_id)
              val explorationFields = fields.filter(field => explorationType.map(_.id.get.toString).contains(field.parent_id))
              val explorationLogs = logs.filter(exploration.id.get.toString == _.child_id).filter(log => explorationFields.map(_.id.get.toString).contains(log.field_id))

              val explorationObj = Json.obj(
                "kind" -> "exploration",
                "entity" -> Json.toJson(explorationType),
                "exploration" -> Json.toJson(exploration),
                "fields" -> Json.toJson(explorationFields),
                "logs" -> Json.toJson(explorationLogs),
                "date" -> explorationLogs.map(_.created_date.get.getMillis()).max
              )
              results = results.+:(explorationObj)
            }
          )

          leads.foreach(
            lead => {
              val leadObj = Json.obj(
                "kind" -> "lead",
                "entity" -> Json.toJson(lead),
                "date" -> lead.updated_date
              )
              results = results.+:(leadObj)
            }
          )

          Ok(results)
        }
      }
  }

  def countStudents(school_ids: List[String], grade: Option[Int], classroom_id: Option[String]) = silhouette.SecuredAction(WithRoles("NormalUser", "Partner") || WithRoles("NormalUser", "Collectivity") || WithRoles("NormalUser", "School")).async {
    implicit request =>
      {
        val querySchool = BSONDocument("id" -> BSONDocument("$in" -> school_ids))
        val queryUser = BSONDocument("config.accountType" -> "student")
        var queryClassroom = BSONDocument()

        if (classroom_id.isDefined) {
          queryClassroom ++= BSONDocument("id" -> classroom_id)
        }

        if (grade.isDefined) {
          queryClassroom ++= BSONDocument("grade" -> grade)
        }

        for {
          schools <- schoolService.find(querySchool)
          classrooms <- classroomService.find(queryClassroom ++ BSONDocument("school_id" -> BSONDocument("$in" -> schools.map(_.id.get.toString))))
          usersCount <- userService.count(queryUser ++ BSONDocument("config.classroom_id" -> BSONDocument("$in" -> classrooms.map(_.id.get.toString))))
        } yield {
          Ok(Json.obj("count" -> Json.toJson(usersCount)))
        }

      }
  }

  def getStudents(school_ids: List[String], grade: Option[Int], classroom_id: Option[String], page: Option[Int], numberPerPage: Option[Int]) = silhouette.SecuredAction(WithRoles("NormalUser", "Partner") || WithRoles("NormalUser", "Collectivity") || WithRoles("NormalUser", "School")).async {
    implicit request =>
      {
        val querySchool = BSONDocument("id" -> BSONDocument("$in" -> school_ids))
        val queryUser = BSONDocument("config.accountType" -> "student")
        var queryClassroom = BSONDocument()

        if (classroom_id.isDefined) {
          queryClassroom ++= BSONDocument("id" -> classroom_id)
        }

        if (grade.isDefined) {
          queryClassroom ++= BSONDocument("grade" -> grade)
        }

        for {
          schools <- schoolService.find(querySchool)
          classrooms <- classroomService.find(queryClassroom ++ BSONDocument("school_id" -> BSONDocument("$in" -> schools.map(_.id.get.toString))))
          usersArray <- userService.find(queryUser ++ BSONDocument("config.classroom_id" -> BSONDocument("$in" -> classrooms.map(_.id.get.toString))), page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25))
        } yield {
          Ok(Json.toJson(usersArray))
        }
      }
  }

  def searchByFullName(school_ids: List[String], grade: Option[Int], classroom_id: Option[String], fullName: String) = silhouette.SecuredAction(WithRoles("NormalUser", "Partner") || WithRoles("NormalUser", "Collectivity") || WithRoles("NormalUser", "School")).async {
    implicit request =>
      {
        val querySchool = BSONDocument("id" -> BSONDocument("$in" -> school_ids))
        var queryClassroom = BSONDocument()

        if (classroom_id.isDefined) {
          queryClassroom ++= BSONDocument("id" -> classroom_id)
        }

        if (grade.isDefined) {
          queryClassroom ++= BSONDocument("grade" -> grade)
        }

        val queryUser = BSONArray(
          BSONDocument(
            "$project" -> BSONDocument(
              "fullName" -> BSONDocument("$concat" -> BSONArray("$firstName", " ", "$lastName")),
              "userID" -> true,
              "firstName" -> true,
              "lastName" -> true,
              "avatar_path" -> true,
              "config" -> true
            )
          ),
          BSONDocument(
            "$match" -> BSONDocument(
              "fullName" -> BSONDocument("$regex" -> fullName, "$options" -> "i"),
              "config.accountType" -> "student"
            )
          )
        )

        for {
          schools <- schoolService.find(querySchool)
          classrooms <- classroomService.find(queryClassroom ++ BSONDocument("school_id" -> BSONDocument("$in" -> schools.map(_.id.get.toString))))
          usersArray <- userService.aggregate(queryUser ++ BSONDocument("$match" -> BSONDocument("config.classroom_id" -> BSONDocument("$in" -> classrooms.map(_.id.get.toString)))))
        } yield {
          Ok(Json.toJson(usersArray))
        }
      }
  }

}
