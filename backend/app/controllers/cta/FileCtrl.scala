package controllers.cta

/**
 * Created by Clément Lelong O8/05/17
 */
import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import models.daos.file.File
import models.daos.page.Page
import models.daos.version.Version
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{ JsArray, JsObject, Json }
import play.api.mvc.{ Action, Controller }
import reactivemongo.bson.BSONDocument
import services.file.FileService
import services.page.PageService
import services.version.VersionService
import utils.mercure.DefaultEnv
import utils.mercure.auth.WithRoles

import scala.collection.mutable.ListBuffer
import scala.util.parsing.json.{ JSONArray, JSONObject }

class FileCtrl @Inject() (
    val messagesApi: MessagesApi,
    val fileService: FileService,
    val versionService: VersionService,
    val pageService: PageService,
    application: play.api.Application,
    silhouette: Silhouette[DefaultEnv]
) extends Controller with I18nSupport {
  val logger = Logger("CC." + this.getClass.getSimpleName);

  import models.daos.file.FileFormats._
  import models.daos.version.VersionFormats._
  import models.daos.page.PageFormats._

  def create = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[File]) {
    implicit request =>
      {
        val file = request.body
        val uid = UUID.randomUUID()

        val docWithId = file.copy(id = Some(uid), created_date = Some(new DateTime()))
        fileService.insert(docWithId).map {
          res =>
            res.inError match {
              case true => {
                InternalServerError(Json.obj("res" -> s" ${res.message}  code: ${res.code}"))
              }
              case false => {

                if (file.kind == "file") {
                  val versionUid = UUID.randomUUID()
                  val version = new Version(
                    id = Some(versionUid),
                    kind = "file",
                    parent_id = uid.toString,
                    name = "Défaut",
                    isDefault = true,
                    created_date = Some(new DateTime())
                  )

                  versionService.insert(version).map {
                    resVersion =>
                      {
                        val pageUid = UUID.randomUUID()
                        val page = new Page(
                          id = Some(pageUid),
                          name = "Défaut",
                          version_id = versionUid.toString,
                          status_name = "open",
                          texts = Some(List("Test")),
                          created_date = Some(new DateTime())
                        )

                        pageService.insert(page)
                      }
                  }
                }

                Created(Json.obj("res" -> Json.obj("id" -> uid.toString)))
              }
            }
        }

      }
  }

  def update(id: String) = silhouette.SecuredAction(WithRoles("NormalUser")).async(parse.json[File]) {

    implicit request =>
      {

        val elemToUpdate = request.body
        val selector = BSONDocument("id" -> id);
        fileService.findAndModify(selector, elemToUpdate, BSONDocument()) map {
          mayOldFile =>
            {
              mayOldFile match {
                case Some(oldFile: File) => {
                  Ok(Json.obj("res" -> "File successfully updated"))
                }
                case _ => InternalServerError(Json.obj("res" -> "error while updating file"))
              }
            }
        }
      }
  }

  def removeByIds(ids: List[String]) = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        fileService.remove(query).map {
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
    kind: Option[String],
    parent_ids: Option[List[String]]
  ) = Action.async {
    implicit request =>
      {
        var query = BSONDocument()

        if (kind.isDefined) {
          query ++= BSONDocument("kind" -> kind)
        }

        if (parent_ids.get.nonEmpty) {
          query ++= BSONDocument("parent_id" -> BSONDocument("$in" -> parent_ids))
        }

        fileService.find(query, page = page, numberPerPage = numberPerPage) map {
          fileArr => Ok(Json.toJson(fileArr))
        }
      }
  }

  def buildTreeList(fileArray: List[File], jsonNode: JsArray, parent_id: String = null, index: Int = 0): JsArray = {

    var tmpJsonNode = jsonNode.copy()

    if (parent_id != null) {
      fileArray.filter(file => file.parent_id.getOrElse("") == parent_id).foreach {
        file =>
          {
            var fileObject = Json.obj(
              "id" -> file.id,
              "name" -> file.name,
              "slug" -> file.slug,
              "kind" -> file.kind,
              "depth" -> index,
              "parent_id" -> file.parent_id.get,
              "created_date" -> file.created_date
            )

            val childArray = Json.arr()

            fileObject ++= Json.obj("children" -> buildTreeList(fileArray, childArray, file.id.get.toString, index + 1))

            tmpJsonNode = tmpJsonNode :+ fileObject
          }
      }

      tmpJsonNode
    } else {
      fileArray.filter(file => !file.parent_id.isDefined).foreach {
        file =>
          {
            var fileObject = Json.obj(
              "id" -> file.id,
              "name" -> file.name,
              "slug" -> file.slug,
              "kind" -> file.kind,
              "depth" -> index,
              "created_date" -> file.created_date
            )

            val childArray = Json.arr()

            fileObject ++= Json.obj("children" -> buildTreeList(fileArray, childArray, file.id.get.toString, index + 1))

            tmpJsonNode = tmpJsonNode :+ fileObject
          }
      }

      tmpJsonNode
    }
  }

  def getTreeList() = silhouette.SecuredAction(WithRoles("Administrator")).async {
    implicit request =>
      {
        val query = BSONDocument()

        fileService.find(query, page = 1, numberPerPage = 10000) map {
          fileArr =>
            {
              Ok(buildTreeList(fileArr, Json.arr()))
            }
        }
      }
  }

  def buildFrontPages(files: List[File], versions: List[Version], pages: List[Page], currentSlug: String = "", parent_id: String = null, jsonArray: JsArray): JsArray = {
    var tmpJsonArray = jsonArray.copy()
    var tmpCurrentSlug = currentSlug

    if (parent_id != null) {
      files.filter(file => file.parent_id.getOrElse("") == parent_id).foreach {
        file =>
          {
            tmpCurrentSlug = if (file.slug != "_") currentSlug + "-" + file.slug else currentSlug

            if (file.kind == "file") {
              val version = versions.find(_.parent_id == file.id.get.toString)

              if (version.isDefined) {
                val page = pages.find(_.version_id == version.get.id.get.toString)

                if (page.isDefined)
                  tmpJsonArray = tmpJsonArray :+ Json.obj(
                    "slug" -> tmpCurrentSlug,
                    "texts" -> page.get.texts.get
                  )
              }
            }

            tmpJsonArray = buildFrontPages(files, versions, pages, tmpCurrentSlug, file.id.get.toString, tmpJsonArray)
          }
      }
    } else {
      files.filter(file => !file.parent_id.isDefined).foreach {
        file =>
          {
            tmpCurrentSlug = if (file.slug != "_") currentSlug + file.slug else currentSlug

            if (file.kind == "file") {
              val version = versions.find(_.parent_id == file.id.get.toString)

              if (version.isDefined) {
                val page = pages.find(_.version_id == version.get.id.get.toString)

                if (page.isDefined)
                  tmpJsonArray = tmpJsonArray :+ Json.obj(
                    "slug" -> tmpCurrentSlug,
                    "texts" -> page.get.texts
                  )
              }
            }

            tmpJsonArray = buildFrontPages(files, versions, pages, tmpCurrentSlug, file.id.get.toString, tmpJsonArray)
          }
      }
    }

    tmpJsonArray
  }

  def getFrontTexts(version_name: Option[String]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument()

        fileService.find(query, page = 1, numberPerPage = 10000) flatMap {
          fileArr =>
            {

              val file_ids = fileArr map (_.id.get.toString)
              val versionDefaultQuery = BSONDocument("kind" -> "file", "parent_id" -> BSONDocument("$in" -> file_ids), "isDefault" -> true)
              var versionNameQuery = BSONDocument("kind" -> "file", "parent_id" -> BSONDocument("$in" -> file_ids))

              if (version_name.isDefined) {
                versionNameQuery ++= BSONDocument("name" -> version_name)
              } else {
                versionNameQuery ++= BSONDocument("isDefault" -> true)
              }

              versionService.find(versionNameQuery, page = 1, numberPerPage = 1000, sort = Json.obj("created_date" -> -1)) flatMap {
                versions =>
                  versionService.find(versionDefaultQuery, page = 1, numberPerPage = 1000, sort = Json.obj("created_date" -> -1)) flatMap {
                    defaultVersions =>
                      {
                        var mergedVersions: ListBuffer[Version] = new ListBuffer[Version]()
                        if (version_name.isDefined) {
                          defaultVersions.foreach(
                            version => {
                              val versionNamed = versions find (_.parent_id == version.parent_id)
                              if (versionNamed.getOrElse(null) != null) {
                                mergedVersions += versionNamed.get
                              } else {
                                mergedVersions += version
                              }
                            }
                          )
                        }

                        val version_ids = if (mergedVersions.length > 0) (mergedVersions.toList map (_.id.get.toString)) else (defaultVersions map (_.id.get.toString))

                        pageService.find(
                          BSONDocument("version_id" -> BSONDocument("$in" -> version_ids), "status_name" -> "open"),
                          page = 1,
                          numberPerPage = 1000,
                          sort = Json.obj("created_date" -> -1)
                        ) map {
                            pages =>
                              {
                                Ok(buildFrontPages(files = fileArr, versions = if (mergedVersions.length > 0) mergedVersions.toList else defaultVersions, pages = pages, jsonArray = Json.arr()))
                              }
                          }
                      }
                  }
              }
            }
        }
      }
  }

  def count(kind: Option[String], parent_ids: Option[List[String]]) = Action.async {
    implicit request =>
      {
        var query = Json.obj()

        if (kind.isDefined) {
          query ++= Json.obj("kind" -> kind)
        }

        if (parent_ids.get.nonEmpty) {
          query ++= Json.obj("parent_id" -> Json.obj("$in" -> parent_ids))
        }

        fileService.count(query).map {
          count => Ok(Json.obj("res" -> count))
        }
      }
  }

  def getByIds(ids: List[String], page: Option[Int], numberPerPage: Option[Int]) = Action.async {
    implicit request =>
      {
        val query = BSONDocument("id" -> BSONDocument("$in" -> ids))
        fileService.find(query, page = page.getOrElse(1), numberPerPage = numberPerPage.getOrElse(25)).flatMap {
          fileArr =>
            {

              val file_ids = fileArr map (_.id.get.toString)
              val versionRequest = BSONDocument("kind" -> "file", "parent_id" -> BSONDocument("$in" -> file_ids))

              for {
                versions <- versionService.find(versionRequest, page = 1, numberPerPage = 1000, sort = Json.obj("created_date" -> -1))
                pages <- pageService.find(BSONDocument("version_id" -> BSONDocument("$in" -> (versions map (_.id.get.toString)))), page = 1, numberPerPage = 1000, sort = Json.obj("created_date" -> -1))
              } yield {
                var jsonFiles = Json.arr()

                fileArr foreach {
                  file =>
                    {

                      var jsonVersions = Json.arr()

                      val fileVersions = versions filter (_.parent_id == file.id.get.toString)

                      fileVersions foreach {
                        version =>
                          {
                            val versionPages = pages filter (_.version_id == version.id.get.toString)
                            var jsonVersion = Json.toJson(version).as[JsObject]
                            jsonVersion += ("pages" -> Json.toJson(versionPages))
                            jsonVersions :+= jsonVersion
                          }
                      }

                      var jsonFile = Json.toJson(file).as[JsObject]
                      jsonFile += ("versions" -> jsonVersions)

                      jsonFiles :+= jsonFile
                    }
                }

                Ok(jsonFiles)
              }
            }
        }
      }
  }

}
