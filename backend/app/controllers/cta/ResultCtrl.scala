package controllers.cta

/**
 * Created by Clément Lelong O8/05/17
 */
import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import models.daos.result.Result
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.result.ResultService
import utils.mercure.DefaultEnv
import utils.mercure.auth.{ IsOwner, WithRoles }

import scala.concurrent.Future

class ResultCtrl @Inject() (
    val messagesApi: MessagesApi,
    val resultService: ResultService,
    application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);

  import models.daos.result.ResultFormats._

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Result]) {
    implicit request =>
      {
        val result = request.body
        val uid = UUID.randomUUID()

        val docWithId = result.copy(id = Some(uid), updated_date = Some(new DateTime()))
        resultService.insert(docWithId).map {
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

  def update(id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Result]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val result = BSONDocument("id" -> id);
        resultService.findAndModify(result, elemToUpdate, BSONDocument()) map {
          mayOldResult =>
            {
              mayOldResult match {
                case Some(oldResult: Result) => {
                  Ok(Json.obj("res" -> "Result successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating result"))
              }
            }
        }
      }
  }

  def createMany(user_id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json) {
    implicit request =>
      {
        (request.body \ "choice_ids").validate[List[String]].fold(
          error => { Future.successful(BadRequest(s"can't find choice_ids / ${error}")) },
          choice_ids =>
            {
              var results: List[Result] = List()
              choice_ids.map(
                choice_id =>
                  {
                    val uid = UUID.randomUUID()
                    results = new Result(Some(uid), choice_id, user_id, Some(new DateTime())) :: results
                  }
              )
              resultService.insertMany(results).map {
                res =>
                  res.ok match {
                    case false => {
                      InternalServerError(Json.obj("res" -> s" ${res.errmsg}  code: ${res.code}"))
                    }
                    case true => {
                      Created(Json.obj("res" -> s"inserted ${results.length} results documents"))
                    }
                  }
              }
            }
        )
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        resultService.remove(query).map {
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
    user_id: Option[String]
  ) = Action.async {
    implicit request =>
      {
        var query = BSONDocument()

        if (user_id.nonEmpty) {
          query ++= BSONDocument("user_id" -> user_id)
        }

        resultService.find(query, page = page, numberPerPage = numberPerPage) map {
          resultArr => Ok(Json.toJson(resultArr))
        }
      }
  }

  def count(user_id: Option[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        var query = Json.obj()

        if (user_id.nonEmpty) {
          query ++= Json.obj("user_id" -> user_id)
        }

        resultService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        resultService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          resultArr => Ok(Json.toJson(resultArr))
        }
      }
  }

}
