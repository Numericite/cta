package modules

import java.util.UUID
import com.google.inject.AbstractModule
import model.mercure.daos.status.Status
import models.daos.notification.{ NotificationDAO, NotificationDAOImpl }
import models.daos.location.{ LocationDAO, LocationDAOImpl }
import models.daos.post.{ PostDAO, PostDAOImpl }
import models.daos.tag.{ TagDAO, TagDAOImpl }
import models.daos.activity.{ ActivityDAO, ActivityDAOImpl }
import models.daos.faq.{ FaqDAO, FaqDAOImpl }
import models.daos.partner.{ PartnerDAO, PartnerDAOImpl }
import models.daos.internship.{ InternshipDAO, InternshipDAOImpl }
import models.daos.company.{ CompanyDAO, CompanyDAOImpl }
import models.daos.meeting.{ MeetingDAO, MeetingDAOImpl }
import models.daos.school.{ SchoolDAO, SchoolDAOImpl }
import models.daos.school.classroom.{ SchoolClassroomDAO, SchoolClassroomDAOImpl }
import models.daos.school.report.{ SchoolReportDAO, SchoolReportDAOImpl }
import models.daos.domain.{ DomainDAO, DomainDAOImpl }
import models.daos.selector.{ SelectorDAO, SelectorDAOImpl }
import models.daos.selection.{ SelectionDAO, SelectionDAOImpl }
import models.daos.choice.{ ChoiceDAO, ChoiceDAOImpl }
import models.daos.result.{ ResultDAO, ResultDAOImpl }
import models.daos.activity.token.{ ActivityTokenDAO, ActivityTokenDAOImpl }
import models.daos.activity.log.{ ActivityLogDAO, ActivityLogDAOImpl }
import models.daos.document.{ DocumentDAO, DocumentDAOImpl }
import models.daos.settings.{ OnisepData, Settings, SettingsDAO, SettingsDAOImpl }
import models.daos.experience.{ ExperienceDAO, ExperienceDAOImpl }
import models.daos.resource.{ ResourceDAO, ResourceDAOImpl }
import models.daos.detail.{ DetailDAO, DetailDAOImpl }
import models.daos.file.{ FileDAO, FileDAOImpl }
import models.daos.version.{ VersionDAO, VersionDAOImpl }
import models.daos.page.{ PageDAO, PageDAOImpl }
import models.daos.course.{ CourseDAO, CourseDAOImpl }
import models.daos.explorationType.{ ExplorationTypeDAO, ExplorationTypeDAOImpl }
import models.daos.exploration.{ ExplorationDAO, ExplorationDAOImpl }
import models.daos.onisep.feedback.{ OnisepFeedbackDAO, OnisepFeedbackDAOImpl }
import models.daos.module.{ ModuleDAO, ModuleDAOImpl }
import models.daos.module.activity.{ ModuleActivityDAO, ModuleActivityDAOImpl }
import models.daos.field.{ FieldDAO, FieldDAOImpl }
import models.daos.fieldlog.{ FieldLogDAO, FieldLogDAOImpl }
import models.daos.lead.{ LeadDAO, LeadDAOImpl }
import models.daos.restriction.{ RestrictionDAO, RestrictionDAOImpl }
import models.daos.association.{ AssociationDAO, AssociationDAOImpl }
import models.daos.loginLog.{ LoginLogDAO, LoginLogDAOImpl }
import models.daos.event.{ EventDAO, EventDAOImpl }
import models.daos.booking.{ BookingDAO, BookingDAOImpl }
import net.codingwell.scalaguice.ScalaModule
import org.joda.time.DateTime
import play.api.Logger
import play.api.libs.concurrent.AkkaGuiceSupport
import reactivemongo.api.indexes.{ Index, IndexType }
import reactivemongo.api.{ FailoverStrategy, MongoConnectionOptions, MongoDriver }
import reactivemongo.bson.{ BSONDocument, BSONInteger }
import reactivemongo.play.json.collection.JSONCollection
import services.notification.{ NotificationService, NotificationServiceImpl }
import services.location.{ LocationService, LocationServiceImpl }
import services.post.{ PostService, PostServiceImpl }
import services.tag.{ TagService, TagServiceImpl }
import services.activity.{ ActivityService, ActivityServiceImpl }
import services.faq.{ FaqService, FaqServiceImpl }
import services.partner.{ PartnerService, PartnerServiceImpl }
import services.internship.{ InternshipService, InternshipServiceImpl }
import services.company.{ CompanyService, CompanyServiceImpl }
import services.meeting.{ MeetingService, MeetingServiceImpl }
import services.school.{ SchoolService, SchoolServiceImpl }
import services.school.classroom.{ SchoolClassroomService, SchoolClassroomServiceImpl }
import services.school.report.{ SchoolReportService, SchoolReportServiceImpl }
import services.domain.{ DomainService, DomainServiceImpl }
import services.selector.{ SelectorService, SelectorServiceImpl }
import services.selection.{ SelectionService, SelectionServiceImpl }
import services.choice.{ ChoiceService, ChoiceServiceImpl }
import services.result.{ ResultService, ResultServiceImpl }
import services.activity.token.{ ActivityTokenService, ActivityTokenServiceImpl }
import services.activity.log.{ ActivityLogService, ActivityLogServiceImpl }
import services.document.{ DocumentService, DocumentServiceImpl }
import services.settings.{ SettingsService, SettingsServiceImpl }
import services.experience.{ ExperienceService, ExperienceServiceImpl }
import services.resource.{ ResourceService, ResourceServiceImpl }
import services.detail.{ DetailService, DetailServiceImpl }
import services.file.{ FileService, FileServiceImpl }
import services.version.{ VersionService, VersionServiceImpl }
import services.page.{ PageService, PageServiceImpl }
import services.course.{ CourseService, CourseServiceImpl }
import services.explorationType.{ ExplorationTypeService, ExplorationTypeServiceImpl }
import services.exploration.{ ExplorationService, ExplorationServiceImpl }
import services.onisep.feedback.{ OnisepFeedbackService, OnisepFeedbackServiceImpl }
import services.module.{ ModuleService, ModuleServiceImpl }
import services.module.activity.{ ModuleActivityService, ModuleActivityServiceImpl }
import services.field.{ FieldService, FieldServiceImpl }
import services.fieldlog.{ FieldLogService, FieldLogServiceImpl }
import services.lead.{ LeadService, LeadServiceImpl }
import services.restriction.{ RestrictionService, RestrictionServiceImpl }
import services.association.{ AssociationService, AssociationServiceImpl }
import services.loginLog.{ LoginLogService, LoginLogServiceImpl }
import services.event.{ EventService, EventServiceImpl }
import services.booking.{ BookingService, BookingServiceImpl }
import utils.scheduler.GlobalActorScheduler

import scala.concurrent.ExecutionContext.Implicits._

/**
 * Provides Guice bindings for the persistence module.
 */

class MainBootModule extends AbstractModule with ScalaModule with AkkaGuiceSupport {

  val logger = Logger("CC." + this.getClass.getSimpleName);
  /**
   * Configures the module.
   */
  def configure() {
    bind[NotificationDAO].to[NotificationDAOImpl]
    bind[NotificationService].to[NotificationServiceImpl]
    bind[LocationDAO].to[LocationDAOImpl]
    bind[LocationService].to[LocationServiceImpl]
    bind[PostDAO].to[PostDAOImpl]
    bind[PostService].to[PostServiceImpl]
    bind[TagDAO].to[TagDAOImpl]
    bind[TagService].to[TagServiceImpl]
    bind[ActivityDAO].to[ActivityDAOImpl]
    bind[ActivityService].to[ActivityServiceImpl]
    bind[FaqDAO].to[FaqDAOImpl]
    bind[FaqService].to[FaqServiceImpl]
    bind[PartnerDAO].to[PartnerDAOImpl]
    bind[PartnerService].to[PartnerServiceImpl]
    bind[SchoolDAO].to[SchoolDAOImpl]
    bind[SchoolService].to[SchoolServiceImpl]
    bind[SchoolClassroomDAO].to[SchoolClassroomDAOImpl]
    bind[SchoolClassroomService].to[SchoolClassroomServiceImpl]
    bind[SchoolReportDAO].to[SchoolReportDAOImpl]
    bind[SchoolReportService].to[SchoolReportServiceImpl]
    bind[DomainDAO].to[DomainDAOImpl]
    bind[DomainService].to[DomainServiceImpl]
    bind[SelectorDAO].to[SelectorDAOImpl]
    bind[SelectorService].to[SelectorServiceImpl]
    bind[SelectionDAO].to[SelectionDAOImpl]
    bind[SelectionService].to[SelectionServiceImpl]
    bind[ChoiceDAO].to[ChoiceDAOImpl]
    bind[ChoiceService].to[ChoiceServiceImpl]
    bind[ResultDAO].to[ResultDAOImpl]
    bind[ResultService].to[ResultServiceImpl]
    bind[ActivityTokenDAO].to[ActivityTokenDAOImpl]
    bind[ActivityTokenService].to[ActivityTokenServiceImpl]
    bind[ActivityLogDAO].to[ActivityLogDAOImpl]
    bind[ActivityLogService].to[ActivityLogServiceImpl]
    bind[DocumentDAO].to[DocumentDAOImpl]
    bind[DocumentService].to[DocumentServiceImpl]
    bind[SettingsDAO].to[SettingsDAOImpl]
    bind[SettingsService].to[SettingsServiceImpl]
    bind[ExperienceDAO].to[ExperienceDAOImpl]
    bind[ExperienceService].to[ExperienceServiceImpl]
    bind[ResourceDAO].to[ResourceDAOImpl]
    bind[ResourceService].to[ResourceServiceImpl]
    bind[DetailDAO].to[DetailDAOImpl]
    bind[DetailService].to[DetailServiceImpl]
    bind[InternshipDAO].to[InternshipDAOImpl]
    bind[InternshipService].to[InternshipServiceImpl]
    bind[CompanyDAO].to[CompanyDAOImpl]
    bind[CompanyService].to[CompanyServiceImpl]
    bind[MeetingDAO].to[MeetingDAOImpl]
    bind[MeetingService].to[MeetingServiceImpl]
    bind[FileDAO].to[FileDAOImpl]
    bind[FileService].to[FileServiceImpl]
    bind[VersionDAO].to[VersionDAOImpl]
    bind[VersionService].to[VersionServiceImpl]
    bind[PageDAO].to[PageDAOImpl]
    bind[PageService].to[PageServiceImpl]
    bind[CourseDAO].to[CourseDAOImpl]
    bind[CourseService].to[CourseServiceImpl]
    bind[OnisepFeedbackDAO].to[OnisepFeedbackDAOImpl]
    bind[OnisepFeedbackService].to[OnisepFeedbackServiceImpl]
    bind[ModuleDAO].to[ModuleDAOImpl]
    bind[ModuleService].to[ModuleServiceImpl]
    bind[ModuleActivityDAO].to[ModuleActivityDAOImpl]
    bind[ModuleActivityService].to[ModuleActivityServiceImpl]
    bind[FieldDAO].to[FieldDAOImpl]
    bind[FieldService].to[FieldServiceImpl]
    bind[FieldLogDAO].to[FieldLogDAOImpl]
    bind[FieldLogService].to[FieldLogServiceImpl]
    bind[ExplorationTypeDAO].to[ExplorationTypeDAOImpl]
    bind[ExplorationTypeService].to[ExplorationTypeServiceImpl]
    bind[ExplorationDAO].to[ExplorationDAOImpl]
    bind[ExplorationService].to[ExplorationServiceImpl]
    bind[LeadDAO].to[LeadDAOImpl]
    bind[LeadService].to[LeadServiceImpl]
    bind[RestrictionDAO].to[RestrictionDAOImpl]
    bind[RestrictionService].to[RestrictionServiceImpl]
    bind[AssociationDAO].to[AssociationDAOImpl]
    bind[AssociationService].to[AssociationServiceImpl]
    bind[LoginLogDAO].to[LoginLogDAOImpl]
    bind[LoginLogService].to[LoginLogServiceImpl]
    bind[EventDAO].to[EventDAOImpl]
    bind[EventService].to[EventServiceImpl]
    bind[BookingDAO].to[BookingDAOImpl]
    bind[BookingService].to[BookingServiceImpl]
    bind(classOf[GlobalActorScheduler]).asEagerSingleton()
    applicationBootstrap()
  }

  def applicationBootstrap() = {

    import com.typesafe.config.ConfigFactory

    import scala.collection.JavaConversions._

    val config = ConfigFactory.load
    val driver = new MongoDriver
    val connection = driver.connection(
      config.getStringList("mongodb.servers"),
      MongoConnectionOptions(),
      Seq()
    )

    //val db = connection.apply(config.getString("mongodb.db"))

    connection.database(config.getString("mongodb.db"), FailoverStrategy.default).map(
      db => {

        db.collection[JSONCollection]("user")
          .indexesManager
          .ensure(
            new Index(
              Seq("userID" -> IndexType(BSONInteger(1))),
              Some("userID"),
              unique = true,
              background = true,
              dropDups = true,
              sparse = false
            )
          ).map(logger info "Index on userID = " + _.toString)

        db.collection[JSONCollection]("user")
          .indexesManager
          .ensure(
            new Index(
              Seq("email" -> IndexType(BSONInteger(1))),
              Some("email"),
              unique = true,
              background = true,
              dropDups = true,
              sparse = false
            )
          ).map(logger info "Index on email = " + _.toString)

        val settingsCollection = db.collection[JSONCollection]("settings")
        settingsCollection.count().map {
          nbSettings =>
            {
              import models.daos.settings.SettingsFormats._

              if (nbSettings <= 0) {
                val setting = new Settings(id = Some(UUID.randomUUID()), new OnisepData(token = ""), extra = Some(BSONDocument()), updated_date = Some(new DateTime()))
                settingsCollection.insert(setting) map (result => logger info s"setting inserted ${result}")
              }
            }
        }
      }
    )

  }

}

