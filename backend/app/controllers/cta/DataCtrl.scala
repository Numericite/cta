package controllers.cta

/**
 * Created by Clément Lelong O8/05/17
 */
import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{ JsObject, Json }
import play.api.libs.ws.WSClient
import play.api.mvc.Controller
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.{ BSONArray, BSONDocument }
import services.activity.log.ActivityLogService
import services.company.CompanyService
import services.experience.ExperienceService
import services.internship.InternshipService
import services.meeting.MeetingService
import services.mercure.user.UserService
import services.school.SchoolService
import services.school.classroom.SchoolClassroomService
import services.lead.LeadService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles
import net.ceedubs.ficus.Ficus._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

class DataCtrl @Inject() (
    val messagesApi: MessagesApi,
    val schoolService: SchoolService,
    val internshipService: InternshipService,
    val meetingService: MeetingService,
    val userService: UserService,
    val schoolClassroomService: SchoolClassroomService,
    val experienceService: ExperienceService,
    val activityLogService: ActivityLogService,
    val companyService: CompanyService,
    val leadService: LeadService,
    implicit val application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);

  import models.daos.company.CompanyFormats._
  import models.daos.internship.InternshipFormats._
  import models.daos.school.SchoolFormats._

  def adminData(
    from_date: Long,
    to_date: Long,
    school_ids: Option[List[String]]
  ): Future[JsObject] = {

    var usersQuery = BSONDocument(
      "config.accountType" -> BSONDocument("$in" -> Seq("student", "teacher"))
    )

    if (school_ids.get.nonEmpty) {
      usersQuery ++= BSONDocument(
        "$or" -> BSONArray(
          BSONDocument("config.school_id" -> BSONDocument("$in" -> school_ids)),
          BSONDocument("config.school_ids" -> BSONDocument("$in" -> school_ids))
        )
      )
    }

    val getUsers = userService.find(usersQuery, page = 1, numberPerPage = 100000) map {
      users => users
    }

    for {
      users <- getUsers
      parentUsers <- userService.find(
        BSONDocument(
          "userID" -> BSONDocument("$in" -> (users map (_.config.get.getAs[String]("parent_id").getOrElse(""))))
        ),
        page = 1,
        numberPerPage = 10000
      )
      mentorUsers <- userService.find(
        BSONDocument(
          "userID" -> BSONDocument("$in" -> (users map (_.config.get.getAs[String]("mentor_id").getOrElse(""))))
        ),
        page = 1,
        numberPerPage = 10000
      )
      schools <- schoolService.find(
        BSONDocument(
          "id" -> BSONDocument(
            "$in" -> (users filter (_.config.get.getAs[String]("accountType").getOrElse("") == "student") map (_.config.get.getAs[String]("school_id").getOrElse("")))
          )
        ),
        page = 1,
        numberPerPage = 10000
      )
    } yield {
      val nbStudents = users count (user => user.config.get.getAs[String]("accountType").getOrElse("") == "student")
      val nbTeachers = users count (user => user.config.get.getAs[String]("accountType").getOrElse("") == "teacher")
      val nbMentors = mentorUsers.length
      val nbParents = parentUsers.length

      val students = users filter (_.config.get.getAs[String]("accountType").getOrElse("") == "student")
      val middleSchoolIds = schools filter (_.kind == "middle-school") map (_.id.get.toString)
      val highSchoolIds = schools filter (_.kind == "high-school") map (_.id.get.toString)
      val highSchoolProIds = schools filter (_.kind == "high-school-pro") map (_.id.get.toString)
      val IMEIds = schools filter (_.kind == "ime") map (_.id.get.toString)
      val ESATIds = schools filter (_.kind == "esat") map (_.id.get.toString)

      val studentStepsOccurrences = (students map (_.config.get.getAs[Int]("currentStep").getOrElse(0))).groupBy(identity).mapValues(_.size).-(0).map {
        case (key, value) => key.toString -> value
      }

      val jsonSchoolKindsOccurrences = Json.obj(
        "middleSchool" -> (
          students count (user => middleSchoolIds.contains(user.config.get.getAs[String]("school_id").getOrElse("none")))
        ),
        "highSchool" -> (
          students count (user => highSchoolIds.contains(user.config.get.getAs[String]("school_id").getOrElse("none")))
        ),
        "highSchoolPro" -> (
          students count (user => highSchoolProIds.contains(user.config.get.getAs[String]("school_id").getOrElse("none")))
        ),
        "ime" -> (
          students count (user => IMEIds.contains(user.config.get.getAs[String]("school_id").getOrElse("none")))
        ),
        "esat" -> (
          students count (user => ESATIds.contains(user.config.get.getAs[String]("school_id").getOrElse("none")))
        )
      )

      Json.obj(
        "admin" -> Json.obj(
          "schoolKindsStudentOccurrences" -> jsonSchoolKindsOccurrences,
          "studentStepsOccurrences" -> studentStepsOccurrences,
          "nbStudents" -> nbStudents,
          "nbMentors" -> nbMentors,
          "nbParents" -> nbParents,
          "nbTeachers" -> nbTeachers
        )
      )
    }
  }

  def collectivityData(
    from_date: Long,
    to_date: Long
  ): Future[JsObject] = {

    val meetingStructureQuery = BSONDocument("kind" -> "structure")

    val getStructureMeetings = meetingService.find(meetingStructureQuery, page = 1, numberPerPage = 10000) map {
      meetings => meetings
    }

    for {
      structureMeetings <- getStructureMeetings
    } yield {
      val nbStructureAware = (structureMeetings map (_.nb_structure_aware.getOrElse(0))).foldLeft(0)(_ + _)

      Json.obj(
        "collectivity" -> Json.obj(
          "nbStructureAware" -> nbStructureAware
        )
      )
    }
  }

  def middleSchoolData(
    from_date: Long,
    to_date: Long,
    school_ids: Option[List[String]],
    grades: Option[List[String]],
    classroom_ids: Option[List[String]]
  ): Future[JsObject] = {
    var schoolQuery = BSONDocument("kind" -> "middle-school")

    if (school_ids.get.nonEmpty) {
      schoolQuery ++= BSONDocument("id" -> BSONDocument("$in" -> school_ids))
    }

    schoolService.find(schoolQuery, page = 1, numberPerPage = 10000) flatMap {
      schools =>
        {
          val school_ids = schools map (_.id.get.toString)
          val internshipQuery = BSONDocument("school_ids" -> BSONDocument("$in" -> school_ids))

          val getInternships = internshipService.find(internshipQuery, page = 1, numberPerPage = 10000) map {
            internships => internships
          }

          for {
            internships <- getInternships
            companies <- companyService.find(
              BSONDocument(
                "id" -> BSONDocument("$in" -> (internships map (_.company_id.get.toString)))
              )
            )
            internshipSchools <- schoolService.find(
              BSONDocument(
                "id" -> BSONDocument("$in" -> (internships map (_.school_ids.getOrElse(Seq()))).flatten)
              )
            )
          } yield {
            val nbImmersions = (internships map (_.nb_immersions.getOrElse(0))).foldLeft(0)(_ + _)
            val nbVisits = (internships map (_.nb_visits.getOrElse(0))).foldLeft(0)(_ + _)
            val nbParticipantsGirl = (internships map (_.nb_participant_girl.getOrElse(0))).foldLeft(0)(_ + _)
            val nbParticipantsBoy = (internships map (_.nb_participant_boy.getOrElse(0))).foldLeft(0)(_ + _)
            val nbSchools = (internships map (_.school_ids.getOrElse(Seq()))).flatten.filter(_ != "0").distinct.length

            Json.obj(
              "middleSchool" ->
                Json.obj(
                  "nbInternships" -> internships.length,
                  "nbImmersions" -> nbImmersions,
                  "nbVisits" -> nbVisits,
                  "nbParticipantsGirl" -> nbParticipantsGirl,
                  "nbParticipantsBoy" -> nbParticipantsBoy,
                  "nbSchools" -> nbSchools,
                  "internships" -> Json.toJson(internships),
                  "companies" -> Json.toJson(companies),
                  "internshipSchools" -> Json.toJson(internshipSchools)
                )
            )
          }
        }
    }
  }

  def highSchoolData(
    from_date: Long,
    to_date: Long,
    school_ids: Option[List[String]],
    grades: Option[List[String]],
    classroom_ids: Option[List[String]]
  ): Future[JsObject] = {
    var schoolQuery = BSONDocument("kind" -> BSONDocument("$in" -> List("high-school", "high-school-pro")))

    if (school_ids.get.nonEmpty) {
      schoolQuery ++= BSONDocument("id" -> BSONDocument("$in" -> school_ids))
    }

    schoolService.find(schoolQuery, page = 1, numberPerPage = 10000) flatMap {
      schools =>
        {
          val school_ids = schools map (_.id.get.toString)
          val meetingSchoolQuery = BSONDocument("kind" -> "school", "school_id" -> BSONDocument("$in" -> school_ids))

          var studentQuery = BSONDocument("config.accountType" -> "student", "config.school_id" -> BSONDocument("$in" -> school_ids))
          var teacherQuery = BSONDocument("config.accountType" -> "teacher", "config.school_id" -> BSONDocument("$in" -> school_ids))

          if (classroom_ids.get.nonEmpty) {
            studentQuery ++= BSONDocument("config.classroom_id" -> BSONDocument("$in" -> classroom_ids))
            teacherQuery ++= BSONDocument("config.classroom_id" -> BSONDocument("$in" -> classroom_ids))
          }

          val schoolClassroomCountQuery = Json.obj("school_id" -> Json.obj("$in" -> school_ids))

          val getSchoolMeetings = meetingService.find(meetingSchoolQuery, page = 1, numberPerPage = 10000) map {
            meetings => meetings
          }

          val getStudents = userService.find(studentQuery, page = 1, numberPerPage = 10000) map {
            students => students
          }

          val getTeachers = userService.find(teacherQuery, page = 1, numberPerPage = 10000) map {
            teachers => teachers
          }

          val getCountClassrooms = schoolClassroomService.count(schoolClassroomCountQuery) map {
            countClassrooms => countClassrooms
          }

          for {
            students <- getStudents
            teachers <- getTeachers
            schoolMeetings <- getSchoolMeetings
            countClassrooms <- getCountClassrooms
            countExperiences <- experienceService.count(
              Json.obj(
                "user_id" ->
                  Json.obj("$in" -> (students map (_.userID.get.toString))),
                "feedback" -> Json.obj("$exists" -> true)
              )
            )
            countActivityLogs <- activityLogService.count(
              Json.obj(
                "user_id" ->
                  Json.obj("$in" -> (students map (_.userID.get.toString))),
                "status_name" -> "open"
              )
            )
            countMentors <- userService.count(
              BSONDocument(
                "userID" ->
                  BSONDocument("$in" -> (students map (_.config.get.getAs[String]("mentor_id").getOrElse("0"))))
              )
            )
          } yield {
            val nbParentInformed = (schoolMeetings map (_.nb_parent_informed.getOrElse(0))).foldLeft(0)(_ + _)
            val domainsOccurrences = (students map (_.config.get.getAs[List[String]]("domain_ids").getOrElse(List("0")))).flatten.groupBy(identity).mapValues(_.size).-("0")
            val streamsOccurrences = (students map (_.config.get.getAs[List[String]]("stream_ids").getOrElse(List("0")))).flatten.groupBy(identity).mapValues(_.size).-("0")

            var jsonData = Json.obj(
              "nbStudents" -> students.length,
              "nbSchools" -> schools.length,
              "nbTeachers" -> teachers.length,
              "nbMentors" -> countMentors,
              "nbClassrooms" -> countClassrooms,
              "nbParentInformed" -> nbParentInformed,
              "nbExperiences" -> countExperiences,
              "nbActivitiesCompleted" -> countActivityLogs,
              "domainsOccurrences" -> domainsOccurrences,
              "streamsOccurrences" -> streamsOccurrences
            )

            if (schools.length == 1 && schools(0).second_year_access_rate.isDefined) {
              jsonData ++= Json.obj("secondYearAccessRate" -> schools(0).second_year_access_rate.get)
            }

            Json.obj(
              "highSchool" -> jsonData
            )
          }
        }
    }
  }

  def getMiddleSchoolData(
    from_date: Long,
    to_date: Long,
    school_ids: Option[List[String]],
    grades: Option[List[String]],
    classroom_ids: Option[List[String]]
  ) = silhouette.SecuredAction(WithRoles("NormalUser", "School")).async {
    implicit request =>
      {
        middleSchoolData(from_date, to_date, school_ids, grades, classroom_ids) map {
          data => Ok(data)
        }
      }
  }

  def getHighSchoolData(
    from_date: Long,
    to_date: Long,
    school_ids: Option[List[String]],
    grades: Option[List[String]],
    classroom_ids: Option[List[String]]
  ) = silhouette.SecuredAction(WithRoles("NormalUser", "School")).async {
    implicit request =>
      {
        highSchoolData(from_date, to_date, school_ids, grades, classroom_ids) map {
          data => Ok(data)
        }
      }
  }

  def getCollectivityData(
    from_date: Long,
    to_date: Long,
    school_ids: Option[List[String]],
    grades: Option[List[String]],
    classroom_ids: Option[List[String]]
  ) = silhouette.SecuredAction(WithRoles("NormalUser", "Collectivity")).async {
    implicit request =>
      {
        val HSDataRequest = highSchoolData(from_date, to_date, school_ids, grades, classroom_ids) map {
          data => data
        }

        val MSDataRequest = middleSchoolData(from_date, to_date, school_ids, grades, classroom_ids) map {
          data => data
        }

        val CollectivityDataRequest = collectivityData(from_date, to_date) map {
          data => data
        }

        for {
          hsData <- HSDataRequest
          msData <- MSDataRequest
          collectivityData <- CollectivityDataRequest
        } yield {
          Ok(msData ++ hsData ++ collectivityData)
        }
      }
  }

  def getAdminData(
    from_date: Long,
    to_date: Long,
    school_ids: Option[List[String]],
    grades: Option[List[String]],
    classroom_ids: Option[List[String]]
  ) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val HSDataRequest = highSchoolData(from_date, to_date, school_ids, grades, classroom_ids) map {
          data => data
        }

        val MSDataRequest = middleSchoolData(from_date, to_date, school_ids, grades, classroom_ids) map {
          data => data
        }

        val AdminDataRequest = adminData(from_date, to_date, school_ids) map {
          data => data
        }

        for {
          hsData <- HSDataRequest
          msData <- MSDataRequest
          adminData <- AdminDataRequest
        } yield {
          Ok(msData ++ hsData ++ adminData)
        }
      }
  }

  def getDataStats(school_ids: List[String], grade: Option[Int], classroom_id: Option[String]) = silhouette.SecuredAction(WithRoles("NormalUser", "Collectivity") || WithRoles("NormalUser", "School") || WithRoles("NormalUser", "Operator")).async {
    implicit request =>
      {
        val querySchool = BSONDocument("id" -> BSONDocument("$in" -> school_ids))
        val queryUser = BSONDocument("config.accountType" -> "student")
        var queryClassroom = BSONDocument()
        val queryLeads = BSONDocument("description" -> BSONDocument("$in" -> List("3ème technologique", "3ème générale")))

        if (classroom_id.isDefined) {
          queryClassroom ++= BSONDocument("id" -> classroom_id)
        }

        if (grade.isDefined) {
          queryClassroom ++= BSONDocument("grade" -> grade)
        }

        for {
          schools <- schoolService.find(querySchool, page = 1, numberPerPage = 1000)
          classrooms <- schoolClassroomService.find(queryClassroom ++ BSONDocument("school_id" -> BSONDocument("$in" -> schools.map(_.id.get.toString))), page = 1, numberPerPage = 1000)
          users <- userService.find(queryUser ++ BSONDocument("config.classroom_id" -> BSONDocument("$in" -> classrooms.map(_.id.get.toString))), page = 1, numberPerPage = 10000)
          leads <- leadService.find(queryLeads ++ BSONDocument("user_id" -> BSONDocument("$in" -> users.map(_.userID.get.toString))), page = 1, numberPerPage = 1000)
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

          val returnJson = Json.obj(
            "countStudents" -> Json.obj(
              "nbStudents" -> users.length,
              "nbSchools" -> schools.length,
              "nbClasses" -> classrooms.length,
              "nbLogin" -> 0.toString
            ),
            "countClassrooms" -> classroomsArray,
            "countLeads" -> leadsArray,
            "countUsersSexes" -> usersSexes,
            "countSchoolsSpecificities" -> schoolsSpecificities,
            "countSchoolsTerritories" -> schoolsTerritories
          )

          Ok(returnJson)

        }
      }
  }

}
