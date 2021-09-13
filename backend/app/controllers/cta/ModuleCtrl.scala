package controllers.cta

import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import models.daos.faq.Faq
import models.daos.module.Module
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.mercure.user.UserService
import services.module.ModuleService
import services.field.FieldService
import services.fieldlog.FieldLogService
import services.module.activity.ModuleActivityService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

import scala.collection.mutable.ListBuffer

/**
 * Created by Clement LELONG on 13/06/19.
 */

class ModuleCtrl @Inject() (
    val messagesApi: MessagesApi,
    val userService: UserService,
    val moduleService: ModuleService,
    val fieldService: FieldService,
    val fieldLogService: FieldLogService,
    implicit val application: play.api.Application,
    passwordHasher: PasswordHasher,
    authInfoRepository: AuthInfoRepository,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("MR." + this.getClass.getSimpleName);

  import models.daos.module.ModuleFormats._

  def create = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[Module]) {
    implicit request =>
      {
        val module = request.body
        val uid = UUID.randomUUID()

        val docWithId = module.copy(id = Some(uid), created_date = Some(new DateTime()), updated_date = Some(new DateTime()))
        moduleService.insert(docWithId).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[Module]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        moduleService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldModule =>
            {
              mayOldModule match {
                case Some(oldModule: Module) => {
                  Ok(Json.obj("res" -> "Module successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating module"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        moduleService.remove(query).map {
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
    grade: Option[Int],
    school_type: Option[String]
  ) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        var query = BSONDocument()

        if (grade.isDefined) {
          query ++= BSONDocument("grade" -> grade)
        }

        if (school_type.isDefined) {
          query ++= BSONDocument("school_type" -> school_type)
        }

        moduleService.find(query, page = page, numberPerPage = numberPerPage) map {
          moduleArr => Ok(Json.toJson(moduleArr))
        }
      }
  }

  def getStudentProgress(user_id: String, module_id: Option[String], grade: Option[Int], school_type: Option[String]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        var moduleQuery = BSONDocument()

        if (module_id.isDefined) {
          moduleQuery ++= BSONDocument("id" -> module_id)
        }

        if (grade.isDefined) {
          moduleQuery ++= BSONDocument("grade" -> grade)
        }

        if (school_type.isDefined) {
          moduleQuery ++= BSONDocument("school_type" -> school_type)
        }

        for {
          modules <- moduleService.find(moduleQuery, page = 1, numberPerPage = 1000)
          fields <- fieldService.find(BSONDocument("parent_id" -> BSONDocument("$in" -> modules.map(_.id.get.toString))), page = 1, numberPerPage = 1000)
          fieldLogs <- fieldLogService.find(BSONDocument("field_id" -> BSONDocument("$in" -> fields.map(_.id.get.toString)), "user_id" -> user_id), page = 1, numberPerPage = 1000)
          fakeFieldLogs <- fieldLogService.find(BSONDocument("field_id" -> "fake_log", "user_id" -> user_id, "child_type" -> "module", "child_id" -> BSONDocument("$in" -> modules.map(_.id.get.toString))), page = 1, numberPerPage = 1000)
        } yield {
          var progress = Json.arr()
          modules.foreach(module => {
            val moduleFields = fields.filter(_.parent_id == module.id.get.toString)
            val moduleLogs = fieldLogs.filter(log => {
              moduleFields.map(_.id.get.toString).contains(log.field_id)
            })

            val moduleFakeLogs = fakeFieldLogs.filter(_.child_id == module.id.get.toString)

            var moduleProgress = Json.obj("module_id" -> module.id.get.toString, "isDone" -> ((moduleLogs.length > 0) || (moduleFakeLogs.length > 0)))

            if (moduleLogs.length > 0) {
              moduleProgress ++= Json.obj("doneDate" -> moduleLogs(0).created_date)
            } else if (moduleFakeLogs.length > 0) {
              moduleProgress ++= Json.obj("doneDate" -> moduleFakeLogs(0).created_date)
            }

            progress = progress.append(moduleProgress)
          })
          Ok(progress)
        }
      }
  }

  def count(grade: Option[Int]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        var query = Json.obj()

        if (grade.isDefined) {
          query ++= Json.obj("grade" -> grade)
        }

        moduleService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        moduleService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          moduleArr => Ok(Json.toJson(moduleArr))
        }
      }
  }

  def getMaxNum(grade: Int) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("grade" -> grade)

        moduleService.find(query) map {
          moduleArr => Ok(Json.toJson(moduleArr.map(_.num).max))
        }
      }
  }

}
