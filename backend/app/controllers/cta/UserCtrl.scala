package controllers.cta

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.mvc.Controller
import reactivemongo.bson.{ BSONArray, BSONDocument }
import play.api.libs.concurrent.Execution.Implicits._
import services.activity.log.ActivityLogService
import services.activity.token.ActivityTokenService
import services.experience.ExperienceService
import services.exploration.ExplorationService
import services.fieldlog.FieldLogService
import services.lead.LeadService
import services.mercure.user.UserService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

import scala.concurrent.Future

/**
 * Created by Clement LELONG on 13/06/19.
 */

class UserCtrl @Inject() (
    val messagesApi: MessagesApi,
    val userService: UserService,
    val activityLogService: ActivityLogService,
    val activityTokenService: ActivityTokenService,
    val experienceService: ExperienceService,
    val fieldLogService: FieldLogService,
    val leadService: LeadService,
    val explorationService: ExplorationService,
    implicit val application: play.api.Application,
    passwordHasher: PasswordHasher,
    authInfoRepository: AuthInfoRepository,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("MR." + this.getClass.getSimpleName);

  import play.api.libs.json.Json

  def reInitUser(id: String, accountType: String) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    request =>
      {

        if (accountType == "student") {

          val query = BSONDocument("userID" -> id)
          val otherEntitiesQuery = BSONDocument("user_id" -> id)

          val unsetQuery = BSONDocument(
            "config.domain_ids" -> "",
            "config.detail_ids" -> "",
            "config.criterion_ids" -> "",
            "config.introspection_stream_ids" -> "",
            "config.stream_ids" -> "",
            "config.sawFinalPopup" -> "",
            "config.sawOnboardingMs" -> "",
            "config.sawModuleVideoMs" -> "",
            "config.sawChoiceVideoMs" -> "",
            "config.sawExplorationVideoMs" -> "",
            "config.sawOnboardingTeacherMs" -> "",
            "config.sawFilesTeacherVideoMs" -> "",
            "config.hasChooseRank3" -> "",
            "config.hasChooseFaculty" -> ""
          )

          val modifier = BSONDocument("$unset" -> unsetQuery)

          val userUpdate = userService.update(query, modifier)
          val activityLogDelete = activityLogService.remove(otherEntitiesQuery)
          val activityTokenDelete = activityTokenService.remove(otherEntitiesQuery)
          val experienceDelete = experienceService.remove(otherEntitiesQuery)
          val fieldLogDelete = fieldLogService.remove(otherEntitiesQuery)
          val leadDelete = leadService.remove(otherEntitiesQuery)
          val explorationDelete = explorationService.remove(otherEntitiesQuery)

          for {
            user <- userUpdate
            activityLog <- activityLogDelete
            activityToken <- activityTokenDelete
            experience <- experienceDelete
            fieldLog <- fieldLogDelete
            lead <- leadDelete
            exploration <- explorationDelete
          } yield {
            Ok("User Student successfully re-init")
          }

        } else if (accountType == "teacher") {

          val query = BSONDocument("userID" -> id)

          val unsetQuery = BSONDocument(
            "config.sawOnboardingTeacherMs" -> "",
            "config.sawFilesTeacherVideoMs" -> ""
          )

          val modifier = BSONDocument("$unset" -> unsetQuery)

          for {
            user <- userService.update(query, modifier)
          } yield {
            Ok("User Teacher successfully re-init")
          }

        } else {
          Future.successful(BadRequest("AccountType problem"))
        }

      }
  }

}
