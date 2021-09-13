package controllers.cta

/**
 * Created by Clément Lelong O8/05/17
 */
import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.typesafe.config.ConfigFactory
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.json.{ JsObject, Json }
import play.api.libs.ws.WSClient
import play.api.mvc.{ Action, Controller }
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles
import net.ceedubs.ficus.Ficus._
import reactivemongo.bson.BSONDocument
import services.settings.SettingsService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class OnisepApiCtrl @Inject() (
    val ws: WSClient,
    val messagesApi: MessagesApi,
    val settingsService: SettingsService,
    implicit val application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);

  def getSchools(size: Int, query: Option[String]) = Action.async {
    implicit request =>
      {
        var params = "size=" + size

        if (query.isDefined) {
          params += "&q=" + query.get
        }

        val url = ConfigFactory.load().as[String]("onisep.rootUrl") + "/dataset/57da952417293/search?" + params

        for {
          settings <- settingsService.find(BSONDocument())
          response <- ws.url(url).withHeaders(
            "Content-Type" -> "application/x-www-form-urlencoded",
            "Authorization" -> s"Bearer ${settings(0).onisep.token}",
            "Application-ID" -> ConfigFactory.load().as[String]("onisep.applicationId")
          ).get()
        } yield {
          Ok(Json.parse(response.body))
        }

      }
  }
}
