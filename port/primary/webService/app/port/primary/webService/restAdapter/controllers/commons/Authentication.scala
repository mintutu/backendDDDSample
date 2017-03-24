package port.primary.webService.restAdapter.controllers.commons

import play.api.mvc.{Request, RequestHeader, Result}
import play.api.mvc.Results._
import play.api.mvc._
import utility.Constant.Time
import utility.Common._

/**
  * Created by specter8x on 3/9/17.
  */
trait Authentication {

  def isAuthorized(request: Request[_]): Boolean = checkSessionData(userSession(request))

  def isAuthorized(request: RequestHeader): Boolean = checkSessionData(request.session.data)

  def userSession[A](request: Request[A]): Map[String, String] = request.session.data

  def getCurrentUserId[A](implicit request: Request[A]): Long =
    request.session("userId").toLong

  private def checkSessionData(m: Map[String, String]): Boolean = {
    val result = for {
      userId <- m.get("userId")
      userTime <- m.get("userTime")
    } yield {
      val isTimedOut = !(currentMilliseconds - userTime.toLong > Time.SESSION_TIMEOUT)
      isTimedOut
    }
    result.getOrElse(false)
    true
  }

  def onUnauthorized(request: Request[_])(result: => Result): Result = {
    if(isAuthorized(request))
      Redirect("Unauthorized").withNewSession
    else
      result
  }

  def isAuthenticated(f: Request[AnyContent] => Result): Action[AnyContent] = Action{ request =>
    if(isAuthorized(request))
      f(request).withSession(request.session - "userTime" + ("userTime" -> currentMilliseconds.toString))
    else
      Unauthorized
  }

}
