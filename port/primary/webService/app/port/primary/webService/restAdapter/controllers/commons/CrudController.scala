package port.primary.webService.restAdapter.controllers.commons

import play.api.Logger
import play.api.i18n.MessagesApi
import play.api.libs.json.{JsNumber, Json, Writes}
import utility.entity.Entity
import play.api.data.Form
import play.api.mvc.{Action, AnyContent}
import utility.exceptions.{BadRequestParameter, DataIntegrityViolationException, EntityNotFound}
import utility.repository.AbstractRepository

import scala.util.{Failure, Success, Try}

/**
  * Created by specter8x on 3/9/17.
  */
trait CrudController[T <: Entity, CreateForm, UpdateForm] extends ControllerHelper
  with FormValidator with Authentication{

  protected val repo: AbstractRepository[T, _]
  protected implicit val jsonWriter: Writes[T]
  protected val message: MessagesApi

  protected val createForm: Form[CreateForm]
  protected val updateForm: Form[UpdateForm]

//  protected val adminRole: Array(UserRole.Ad)
  protected def createByForm(f: CreateForm): Try[T]
  protected def updateByForm(f: UpdateForm, t: T): Try[T]

  def getAll(allow_inactive: Option[String]): Action[AnyContent] = isAuthenticated {
    implicit request =>
      val allowInactive = allow_inactive match {
        case None => false
        case Some("y") => true
        case Some(_) => false
      }
      repo.getAll(allowInactive = allowInactive) match {
        case Success(list) => Ok(Json.toJson(list.sortBy(_.identity)))
        case Failure(e) => logUnknownError(e)
      }
  }

  def getById(id: Long): Action[AnyContent] = isAuthenticated {
    implicit request =>
      repo.getById(id, allowInactive = false) match {
        case Success(entity) => Ok(Json.toJson(entity))
        case Failure(e) => e match {
          case _: EntityNotFound => notFound(id)
          case _ => logUnknownError(e)
        }
      }
  }

  def update(id: Long): Action[AnyContent] = isAuthenticated {
    implicit request =>
      val result = for {
        updateForm <- validateForm(updateForm)
        oldEntity <- repo.getById(id, allowInactive = true)
        newEntity <- updateByForm(updateForm, oldEntity)
        result <- if (oldEntity == newEntity) Success(false) else repo.update(newEntity)
      } yield result
      result match {
        case Success(_) => Ok(successJson(JsNumber(id), "Successfully updated item with id: " + id))
        case Failure(e) => e match {
          case _: BadRequestParameter => {
              Logger.error("Bad Request: " + e.getMessage)
              badRequest("Bad Request")
          }
          case _: EntityNotFound => notFound(id)
          case _: DataIntegrityViolationException => badRequest(message(e.getMessage))
          case _ => logUnknownError(e)
        }
      }
  }

  def create(): Action[AnyContent] = isAuthenticated {
    implicit request =>
      val result = for {
        createForm <- validateForm(createForm)
        entity <- createByForm(createForm)
        id <- repo.insert(entity)
      } yield id
      result match {
        case Success(id) => Ok(successJson(JsNumber(id), "Successfully updated item with id: " + id))
        case Failure(e) => e match {
          case _: BadRequestParameter => badRequest("Invalid request form for " + this.getClass.toString)
          case _: DataIntegrityViolationException => badRequest(message(e.getMessage))
          case _ => logUnknownError(e)
        }
      }
  }
}
