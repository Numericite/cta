package services.scheduler

/**
 * Created by Clément Lelong on 23/06/16.
 */
import java.sql.Timestamp

import akka.actor.Actor
import com.google.inject.Inject
import com.typesafe.config.ConfigFactory
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.{ Application, Logger }
import services.mercure.user.UserService
import services.mercure.status.StatusService
import services.location.LocationService
import utils.mercure.mail.MailService
import net.ceedubs.ficus.Ficus._
import play.api.libs.json.Json
import play.api.libs.ws._
import reactivemongo.bson.BSONDocument
import services.settings.SettingsService

import scala.concurrent.ExecutionContext.Implicits.global

class BasicScheduler(
    val userService: UserService,
    val statusService: StatusService,
    val locationService: LocationService,
    val mailService: MailService,
    val messagesApi: MessagesApi,
    val settingsService: SettingsService,
    val application: Application,
    val ws: WSClient
) extends Actor with I18nSupport {

  val logger = Logger("CC." + this.getClass.getSimpleName);
  implicit val ms: MailService = mailService;
  implicit val m: MessagesApi = messagesApi;

  def receive = {
    case "MONGODUMP" => {
      import scala.sys.process._
      val appPath = application.path.getPath
      val dbName = ConfigFactory.load().as[String]("mongodb.db")
      val filePath = appPath + "/conf/mongodumper.sh"
      val cmd = s"sh $filePath $appPath $dbName"
      //val dump = cmd.lineStream_!
      //logger.debug("BACKUP MONGO OUTPUT = " + dump)
      logger.info("DUMP MONGO DONE")
    }
    case "GENERATE_TOKEN_ONISEP" => {
      val url = ConfigFactory.load().as[String]("onisep.rootUrl") + "/login"

      val params = "email=" +
        ConfigFactory.load().as[String]("onisep.login") +
        "&password=" + ConfigFactory.load().as[String]("onisep.password")

      ws.url(url).withHeaders("Content-Type" -> "application/x-www-form-urlencoded").post(
        params
      ).map {
        response =>
          {
            (Json.parse(response.body) \ "token").validate[String].fold(
              error => { logger.error(s"CANNOT GET ONISEP TOKEN : ${error}") },
              token => settingsService.find(BSONDocument()) map {
                settings =>
                  {
                    val setting = settings(0)

                    val selector = BSONDocument("id" -> setting.id.get.toString)
                    val updateSettings = BSONDocument(
                      "$set" -> BSONDocument(
                        "onisep.token" -> token
                      )
                    )

                    settingsService.update(selector, updateSettings) map {
                      response => logger.info(s"new Onisep TOKEN save : ${token}")
                    }
                  }
              }
            )
          }
      }
    }
  }
}

