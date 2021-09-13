package controllers.cta

/**
 * Created by Clément Lelong O8/05/17
 */
import java.util.UUID
import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.typesafe.config.ConfigFactory
import fly.play.s3.{ BucketFile, S3, S3Exception }
import net.ceedubs.ficus.Ficus._
import models.daos.partner.Partner
import org.apache.commons.io.FileUtils
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.{ BSONArray, BSONDocument }
import services.partner.PartnerService
import services.mercure.user.UserService
import services.school.classroom.SchoolClassroomService
import services.school.SchoolService
import services.exploration.ExplorationService
import services.explorationType.ExplorationTypeService
import services.restriction.RestrictionService
import services.fieldlog.FieldLogService
import services.association.AssociationService
import services.lead.LeadService
import services.loginLog.LoginLogService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

import scala.concurrent.Future

class PartnerCtrl @Inject() (
    val messagesApi: MessagesApi,
    val partnerService: PartnerService,
    val userService: UserService,
    val schoolService: SchoolService,
    val classroomService: SchoolClassroomService,
    val explorationService: ExplorationService,
    val explorationTypeService: ExplorationTypeService,
    val restrictionService: RestrictionService,
    val fieldLogService: FieldLogService,
    val leadService: LeadService,
    val associationService: AssociationService,
    val loginLogService: LoginLogService,
    implicit val application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);

  import models.daos.partner.PartnerFormats._
  import models.daos.explorationType.ExplorationTypeFormats._
  import models.daos.restriction.RestrictionFormats._

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Partner]) {
    implicit request =>
      {
        val partner = request.body
        val uid = UUID.randomUUID()

        val partnerSlug = BSONDocument("slug" -> partner.slug)

        partnerService.find(partnerSlug).flatMap {
          partners =>
            {

              if (partners.nonEmpty) {

                Future.successful(
                  Conflict(
                    Json.obj("res" ->
                      Json.obj(
                        "messsage" -> "partner slug already exist with another provider",
                        "slug" -> partners.head.slug
                      ))
                  )
                )

              } else {

                val docWithId = partner.copy(id = Some(uid), updated_date = Some(new DateTime()))
                partnerService.insert(docWithId).map {
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
        }

      }
  }

  def update(id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Partner]) {

    implicit request =>
      {
        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);

        partnerService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldPartner =>
            {
              mayOldPartner match {
                case Some(oldPartner: Partner) => {
                  Ok(Json.obj("res" -> "Partner successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating partner"))
              }
            }
        }

      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        partnerService.remove(query).map {
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
    numberPerPage: Int
  ) = Action.async {
    implicit request =>
      {
        val query = BSONDocument()

        partnerService.find(query, page = page, numberPerPage = numberPerPage) map {
          partnerArr => Ok(Json.toJson(partnerArr))
        }
      }
  }

  def count() = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = Json.obj()

        partnerService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        partnerService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          partnerArr => Ok(Json.toJson(partnerArr))
        }
      }
  }

  def uploadLogo(partner_id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.multipartFormData) {
    request =>
      request.body.file("file").map { file =>
        import java.io.File
        val currentPath = application.path.getAbsolutePath
        val filename = file.filename.replace(' ', '_')
        val filePath = currentPath + "/partner/" + partner_id + "/" + filename;
        val fileUrl = "logos/partner/" + partner_id + "/" + filename;
        val contentType = file.contentType
        //val title = request.body.asFormUrlEncoded.get("title").get(0);
        val dir = new File(currentPath + "/partner/" + partner_id)
        dir.mkdirs()
        logger.debug(s"Application cur path [$currentPath]")
        val fileFromDisk = file.ref.moveTo(new File(filePath))

        val bucketName = ConfigFactory.load().as[String]("s3.bucketName")
        val bucket = S3(bucketName)
        val byteArr = FileUtils.readFileToByteArray(fileFromDisk)
        val result = bucket add BucketFile(fileUrl, file.contentType.getOrElse("text/plain"), byteArr, None, None)
        result.flatMap {
          unit =>
            {
              val awsFileUrl = bucket.s3.url(bucketName, fileUrl)
              logger.debug(s"Aws result $unit")

              val updatedPartner = BSONDocument(
                "$set" -> BSONDocument(
                  "img_path" -> awsFileUrl
                )
              )

              val selec = BSONDocument("id" -> partner_id);
              partnerService.update(selec, updatedPartner) map {
                res =>
                  res.inError match {
                    case true => {
                      InternalServerError(Json.obj("res" -> s" ${res.message}  code: ${res.code}"))
                    }
                    case false => {
                      Ok(Json.obj("res" -> Json.obj("path" -> awsFileUrl)))
                    }
                  }
              }

            }
        }.recover {
          case S3Exception(status, code, message, originalXml) =>
            {
              logger.error("Error: " + message)
              BadRequest(Json.obj("result" -> "main logo error: missing file or existing file"))
            }
        }

      }.getOrElse {
        Future.successful(BadRequest(Json.obj("result" -> "atelier error: missing file or existing file")))
      }
  }

  def countExplorationsType(partner_slug: String, school_ids: List[String], grade: Option[Int], classroom_id: Option[String], name: Option[String]) = silhouette.SecuredAction(WithRoles("NormalUser", "Partner")).async {
    implicit request =>
      {
        var queryExplorationType = Json.obj("partner_slug" -> partner_slug)
        var queryRestriction = BSONDocument()

        if (school_ids.nonEmpty) {
          queryRestriction ++= BSONDocument("school_id" -> BSONDocument("$in" -> ("" :: school_ids)))
        }

        if (grade.isDefined) {
          queryRestriction ++= BSONDocument("$or" -> BSONArray(BSONDocument("grade" -> grade), BSONDocument("grade" -> BSONDocument("$exists" -> false))))
        }

        if (classroom_id.isDefined) {
          queryRestriction ++= BSONDocument("classroom_id" -> BSONDocument("$in" -> List("", classroom_id.getOrElse(""))))
        }

        if (name.isDefined) {
          queryExplorationType ++= Json.obj("name" -> Json.obj("$regex" -> name, "$options" -> "i"))
        }

        for {
          restrictionsArray <- restrictionService.find(queryRestriction, page = 1, numberPerPage = 1000)
          explorationsTypeCount <- explorationTypeService.count(queryExplorationType ++ Json.obj("id" -> Json.obj("$in" -> restrictionsArray.map(_.exploration_type_id))))
        } yield {
          Ok(Json.obj("count" -> Json.toJson(explorationsTypeCount)))
        }

      }
  }

  def getExplorationsType(partner_slug: String, school_ids: List[String], grade: Option[Int], classroom_id: Option[String], name: Option[String], page: Option[Int], numberPerPage: Option[Int]) = silhouette.SecuredAction(WithRoles("NormalUser", "Partner")).async {
    implicit request =>
      {
        var queryExplorationType = BSONDocument("partner_slug" -> partner_slug)
        var queryRestriction = BSONDocument()

        if (school_ids.nonEmpty) {
          queryRestriction ++= BSONDocument("school_id" -> BSONDocument("$in" -> ("" :: school_ids)))
        }

        if (grade.isDefined) {
          queryRestriction ++= BSONDocument("$or" -> BSONArray(BSONDocument("grade" -> grade), BSONDocument("grade" -> BSONDocument("$exists" -> false))))
        }

        if (classroom_id.isDefined) {
          queryRestriction ++= BSONDocument("classroom_id" -> BSONDocument("$in" -> List("", classroom_id.getOrElse(""))))
        }

        if (name.isDefined) {
          queryExplorationType ++= BSONDocument("name" -> BSONDocument("$regex" -> name, "$options" -> "i"))
        }

        for {
          restrictionsArray <- restrictionService.find(queryRestriction, page = 1, numberPerPage = 1000)
          explorationsTypeArray <- explorationTypeService.find(queryExplorationType ++ BSONDocument("id" -> BSONDocument("$in" -> restrictionsArray.map(_.exploration_type_id))), page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25))
        } yield {
          Ok(
            Json.toJson(
              explorationsTypeArray
            )
          )
        }

      }
  }

  def getPartnerStats(partner_slug: Option[String], school_ids: Option[List[String]], grade: Option[Int], classroom_id: Option[String], kind: Option[String]) = silhouette.SecuredAction(WithRoles("NormalUser", "Partner")).async {
    implicit request =>
      {
        var querySchool = BSONDocument()
        val queryUser = BSONDocument("config.accountType" -> "student")
        var queryClassroom = BSONDocument()
        var queryExplorationType = BSONDocument()
        val queryLeads = BSONDocument("description" -> BSONDocument("$in" -> List("3ème technologique", "3ème générale")))
        var queryAssociationsCount = Json.obj()

        if (kind.isDefined) {
          querySchool ++= BSONDocument("kind" -> kind)
        }

        if (partner_slug.isDefined) {
          queryExplorationType ++= BSONDocument("partner_slug" -> partner_slug)
          queryAssociationsCount ++= Json.obj("partner_slug" -> partner_slug)
        }

        if (school_ids.get.nonEmpty) {
          querySchool ++= BSONDocument("id" -> BSONDocument("$in" -> school_ids))
        }

        if (classroom_id.isDefined) {
          queryClassroom ++= BSONDocument("id" -> classroom_id)
        }

        if (grade.isDefined) {
          queryClassroom ++= BSONDocument("grade" -> grade)
        }

        for {
          schools <- schoolService.find(querySchool, page = 1, numberPerPage = 1000)
          classrooms <- classroomService.find(queryClassroom ++ BSONDocument("school_id" -> BSONDocument("$in" -> schools.map(_.id.get.toString))), page = 1, numberPerPage = 1000)
          users <- userService.find(queryUser ++ BSONDocument("config.classroom_id" -> BSONDocument("$in" -> classrooms.map(_.id.get.toString))), page = 1, numberPerPage = 10000)
          explorationTypes <- explorationTypeService.find(queryExplorationType, page = 1, numberPerPage = 1000)
          explorations <- explorationService.find(BSONDocument("exploration_type_id" -> BSONDocument("$in" -> explorationTypes.map(_.id.get.toString))) ++ BSONDocument("user_id" -> BSONDocument("$in" -> users.map(_.userID.get.toString))), page = 1, numberPerPage = 1000)
          leads <- leadService.find(queryLeads ++ BSONDocument("user_id" -> BSONDocument("$in" -> users.map(_.userID.get.toString))), page = 1, numberPerPage = 1000)
          loginLogCount <- loginLogService.count(Json.obj("user_id" -> Json.obj("$in" -> users.map(_.userID.get.toString))))
          associationsCount <- associationService.count(queryAssociationsCount)
          fieldLogs <- fieldLogService.aggregate(BSONArray(
            BSONDocument(
              "$match" -> BSONDocument(
                "child_id" -> BSONDocument("$in" -> explorations.map(_.id.get.toString))
              )
            ),
            BSONDocument(
              "$group" -> BSONDocument(
                "_id" -> "$item",
                "uniqueValues" -> BSONDocument(
                  "$addToSet" -> "$user_id"
                ),
                "count" -> BSONDocument("$sum" -> 1)
              )
            )
          ))
        } yield {

          var classroomsArray = Json.arr()
          classrooms.groupBy(_.grade).foreach(
            classroom => if (classroom._1.toString.nonEmpty) classroomsArray = classroomsArray :+ Json.obj("name" -> classroom._1, "length" -> classroom._2.length)
          )

          var leadsArray = Json.arr()
          leads.groupBy(_.description).foreach(
            lead => if (lead._1.nonEmpty) leadsArray = leadsArray :+ Json.obj("name" -> lead._1, "length" -> lead._2.length)
          )

          var usersSexes = Json.arr()
          users.groupBy(_.sex).foreach(
            user => if (user._1.nonEmpty & user._1 != "undefined") usersSexes = usersSexes :+ Json.obj("name" -> user._1, "length" -> user._2.length)
          )

          var schoolsSpecificities = Json.arr()
          schools.groupBy(_.specificity).foreach(
            school => if (school._1.isDefined) schoolsSpecificities = schoolsSpecificities :+ Json.obj("name" -> school._1.get, "length" -> school._2.length)
          )

          var schoolsTerritories = Json.arr()
          schools.groupBy(_.territory).foreach(
            school => if (school._1.isDefined) schoolsTerritories = schoolsTerritories :+ Json.obj("name" -> school._1.get, "length" -> school._2.length)
          )

          var explorationCategories = Json.arr()
          explorationTypes.groupBy(_.category).foreach(
            exploration => if (exploration._1.isDefined) explorationCategories = explorationCategories :+ Json.obj("name" -> exploration._1.get, "length" -> exploration._2.length)
          )

          val returnJson = Json.obj(
            "countStudents" -> Json.obj(
              "nbStudents" -> users.length,
              "nbSchools" -> schools.length,
              "nbClasses" -> classrooms.length,
              "nbAssociations" -> associationsCount
            ),
            "countExplorations" -> Json.obj(
              "nbExplorationsCreated" -> explorations.length,
              "nbExplorationsCompleted" -> (fieldLogs \ "result" \ 0 \ "count").getOrElse(Json.toJson(0)),
              "nbLogin" -> loginLogCount
            ),
            "countClassrooms" -> classroomsArray,
            "countLeads" -> leadsArray,
            "countUsersSexes" -> usersSexes,
            "countSchoolsSpecificities" -> schoolsSpecificities,
            "countSchoolsTerritories" -> schoolsTerritories,
            "countExplorationCategories" -> explorationCategories
          )
          Ok(returnJson)
        }
      }
  }

}
