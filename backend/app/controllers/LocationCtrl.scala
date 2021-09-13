
package controllers

import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import models.daos.location.Location
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.location.LocationService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles
import play.api.libs.concurrent.Execution.Implicits._

/**
 * Created by Clément Lelong O8/05/17
 */
class LocationCtrl @Inject() (
    val messagesApi: MessagesApi,
    val locationService: LocationService,
    application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {

  val logger = Logger("IDP." + this.getClass.getSimpleName);

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[Location]) {
    implicit request =>
      {
        val uID = UUID.randomUUID()
        val location = request.body.copy(id = Some(uID))
        locationService.insert(location).map {
          res =>
            res.inError match {
              case true => {
                InternalServerError(Json.obj("res" -> s" ${res.message}  code: ${res.code}"))
              }
              case false => {
                Created(Json.obj("res" -> Json.obj("id" -> uID.toString)))
              }
            }
        }
      }
  }

  def updateLocation(id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async(parse.json[Location]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        locationService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldSub =>
            {
              mayOldSub match {
                case Some(oldSub: Location) => {
                  Ok(Json.obj("res" -> "Subject successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating subject"))
              }
            }
        }
      }
  }

  def locationList(page: Int, numberPerPage: Int) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        locationService.getList(page, numberPerPage) map {
          attArr => Ok(Json.toJson(attArr))
        }
      }
  }

  def search(state: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = BSONDocument("state" -> BSONDocument("$regex" -> state, "$options" -> "i"), "district" -> "", "city" -> "", "street" -> "")
        locationService.find(query).map {
          listLocation => Ok(Json.toJson(listLocation))
        }
      }
  }

  def exists(country: String, district: String, city: String, state: String, street: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async {
    implicit request =>
      {
        val query = BSONDocument("district" -> district, "city" -> city, "state" -> state, "street" -> street, "country" -> country)
        locationService.findOne(query).map {
          case location if (location.isDefined) => Ok(Json.obj("id" -> location.get.id.get))
          case location if (location.isEmpty) => NotFound
        }
      }
  }

  def removeById(id: String) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> id)
        locationService.remove(query).map {
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

  def getLocationByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        locationService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).map {
          attArr => Ok(Json.toJson(attArr))
        }
      }
  }

}
