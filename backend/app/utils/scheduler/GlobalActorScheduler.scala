package utils.scheduler

/**
 * Created by Clément Lelong on 23/06/16.
 */

import java.time.LocalTime

import javax.inject.Inject
import akka.actor.{ ActorSystem, Props }
import com.typesafe.config.ConfigFactory
import play.api.i18n.MessagesApi
import play.api.libs.concurrent.Execution.Implicits._
import services.mercure.user.UserService
import services.mercure.status.StatusService
import services.scheduler.BasicScheduler
import play.api.{ Application, Logger }
import utils.mercure.mail.MailService
import net.ceedubs.ficus.Ficus._
import play.api.libs.ws.WSClient
import services.location.LocationService
import services.settings.SettingsService

class GlobalActorScheduler @Inject() (
    implicit
    app: Application,
    val actorSystem: ActorSystem,
    val userService: UserService,
    val statusService: StatusService,
    val locationService: LocationService,
    val mailService: MailService,
    val messagesApi: MessagesApi,
    val settingsService: SettingsService,
    val ws: WSClient
) {
  import scala.concurrent.duration._

  val logger = Logger("CC." + this.getClass.getSimpleName);
  val scheduleActor = actorSystem.actorOf(Props(classOf[BasicScheduler], userService, statusService, locationService, mailService, messagesApi, settingsService, app, ws))

  /* MONGO DUMP */
  logger.debug("INIT MONGO DUMP SCHEDULER")
  val startTimeMongo = ConfigFactory.load().as[FiniteDuration]("mongodump.startTime")
  val repeatIntervalMongo = ConfigFactory.load().as[FiniteDuration]("mongodump.repeatInterval")
  actorSystem.scheduler.schedule(startTimeMongo, repeatIntervalMongo, scheduleActor, "MONGODUMP")

  /* REFRESH ONISEP TOKEN EVERY 24H */
  val interval = 24.hours
  val delay = {
    val time = LocalTime.of(2, 0).toSecondOfDay
    val now = LocalTime.now().toSecondOfDay
    val fullDay = 60 * 60 * 24
    val difference = time - now
    if (difference < 0) {
      fullDay + difference
    } else {
      time - now
    }
  }.seconds
  actorSystem.scheduler.schedule(delay, interval, scheduleActor, "GENERATE_TOKEN_ONISEP")
}

