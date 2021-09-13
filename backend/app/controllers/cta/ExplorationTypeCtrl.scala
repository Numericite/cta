package controllers.cta

import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import models.daos.explorationType.ExplorationType
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{ JsString, Json }
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.mercure.user.UserService
import services.explorationType.ExplorationTypeService
import services.school.classroom.SchoolClassroomService
import services.restriction.RestrictionService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

/**
 * Created by Clement LELONG on 13/06/19.
 */

class ExplorationTypeCtrl @Inject() (
    val messagesApi: MessagesApi,
    val userService: UserService,
    val explorationTypeService: ExplorationTypeService,
    val schoolClassroomService: SchoolClassroomService,
    val restrictionService: RestrictionService,
    implicit val application: play.api.Application,
    passwordHasher: PasswordHasher,
    authInfoRepository: AuthInfoRepository,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("MR." + this.getClass.getSimpleName);

  import models.daos.explorationType.ExplorationTypeFormats._

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[ExplorationType]) {
    implicit request =>
      {
        val explorationType = request.body
        val uid = UUID.randomUUID()

        val docWithId = explorationType.copy(id = Some(uid), created_date = Some(new DateTime()), updated_date = Some(new DateTime()))
        explorationTypeService.insert(docWithId).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[ExplorationType]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        explorationTypeService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldExplorationType =>
            {
              mayOldExplorationType match {
                case Some(oldExplorationType: ExplorationType) => {
                  Ok(Json.obj("res" -> "ExplorationType successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating explorationType"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        explorationTypeService.remove(query).map {
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
    partner_slug: Option[String],
    exclude_partner: Boolean
  ) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        var query = BSONDocument()

        if (exclude_partner) {
          query ++= BSONDocument("partner_slug" -> BSONDocument("$exists" -> !exclude_partner))
        }

        if (partner_slug.isDefined) {
          query ++= BSONDocument("partner_slug" -> partner_slug)
        }

        explorationTypeService.find(query, page = page, numberPerPage = numberPerPage) map {
          explorationTypeArr => Ok(Json.toJson(explorationTypeArr))
        }
      }
  }

  def count(partner_slug: Option[String]) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        var query = Json.obj()

        if (partner_slug.isDefined) {
          query ++= Json.obj("partner_slug" -> partner_slug)
        }

        explorationTypeService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        explorationTypeService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          explorationTypeArr => Ok(Json.toJson(explorationTypeArr))
        }
      }
  }

  def getListWithRestriction(classroom_id: String) = Action.async {
    implicit request =>
      {

        val query = BSONDocument("id" -> classroom_id)

        val requestSchoolClassroom = schoolClassroomService.find(query)

        for {
          classroom <- requestSchoolClassroom
          restrictions1 <- restrictionService.find(BSONDocument("classroom_id" -> classroom.head.id.get.toString))
          restrictions2 <- restrictionService.find(BSONDocument("school_id" -> classroom.head.school_id, "grade" -> classroom.head.grade, "classroom_id" -> ""))
          restrictions3 <- restrictionService.find(BSONDocument("school_id" -> "", "grade" -> classroom.head.grade, "classroom_id" -> ""))
          restrictions4 <- restrictionService.find(BSONDocument("school_id" -> "", "grade" -> BSONDocument("$exists" -> false), "classroom_id" -> ""))
          restrictions5 <- restrictionService.find(BSONDocument("school_id" -> classroom.head.school_id, "grade" -> BSONDocument("$exists" -> false), "classroom_id" -> ""))
          explorations <- explorationTypeService.find(BSONDocument("id" -> BSONDocument("$in" -> (restrictions1 ++ restrictions2 ++ restrictions3 ++ restrictions4 ++ restrictions5).map(_.exploration_type_id)), "status_name" -> "open"))
        } yield {
          Ok(Json.toJson(explorations))
        }

      }
  }

}
