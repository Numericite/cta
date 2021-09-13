package controllers.cta

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.Controller
import reactivemongo.bson.BSONDocument
import services.mercure.user.UserService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

/**
 * Created by Clement LELONG on 13/06/19.
 */

class ParentCtrl @Inject() (
    val messagesApi: MessagesApi,
    val userService: UserService,
    implicit val application: play.api.Application,
    passwordHasher: PasswordHasher,
    authInfoRepository: AuthInfoRepository,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("MR." + this.getClass.getSimpleName);

  import play.api.libs.json.Json

  def getChildren(parent_id: String, page: Int, numberPerPage: Int) = silhouette.SecuredAction(WithRoles("NormalUser", "Parent")).async {
    implicit request =>
      {
        val query = BSONDocument("config.parent_id" -> parent_id, "config.accountType" -> "student")

        userService.find(query, Json.obj("updated_date" -> -1), page, numberPerPage) map {
          userArr => Ok(Json.obj("res" -> Json.toJson(userArr)))
        }
      }
  }

  def getParents(page: Int, numberPerPage: Int) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("config.accountType" -> "parent")

        userService.find(query, Json.obj("updated_date" -> -1), page, numberPerPage) map {
          userArr => Ok(Json.obj("res" -> Json.toJson(userArr)))
        }
      }
  }
}
