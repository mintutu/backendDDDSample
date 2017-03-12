package restAdapter.controllers.commons

import java.io.{PrintWriter, StringWriter}

import domain.masterData.user.User
import play.api.Logger
import play.api.mvc.Controller
import play.api.mvc.{Controller, Result}
import utility.exceptions.UnauthorizedException

/**
  * Created by specter8x on 3/9/17.
  */
trait ControllerHelper extends Controller with JsonSupport{
  protected def logUnknownError(e: Throwable): Result = {
    val sWriter = new StringWriter()
    e.printStackTrace(new PrintWriter(sWriter))
    Logger.error(s"Unknown error in ${this.getClass.toString} : " + e.getMessage)
    Logger.error("Stacktrace:\n" + sWriter.toString)
    InternalServerError(errorJson("common.internal_server_error", "Internal Server Error"))
  }

  protected def logUnauthorised(e: UnauthorizedException, user: User): Result = {
    val sWriter = new StringWriter()
    e.printStackTrace(new PrintWriter(sWriter))
    Logger.warn(s"Unauthorized access by user with id ${user.userId} in: ${this.getClass.toString}: " + e.getMessage)
    Logger.warn("Stacktrace:\n" + sWriter.toString)
    Unauthorized
  }

  protected def notFound(id: Long): Result = {
    NotFound(errorJson("common.not_found", "Couldn't found item with id: " + id.toString))
  }

  protected def badRequest(message: String, e: Option[Exception] = None): Result = {
    Logger.warn(s"Bad request received ${e.map(_.toString).getOrElse("")}")
    BadRequest(message)
  }
}
