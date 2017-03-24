package port.primary.webService.restAdapter.controllers.authentication

import com.google.inject.Inject
import domain.masterData.user.{Credential, User, UserRepository, UserService}
import play.api.Logger
import play.api.mvc.{Action, AnyContent, Controller}
import play.api.mvc._
import port.primary.webService.restAdapter.controllers.commons.{Authentication, FormValidator}
import port.primary.webService.restAdapter.forms.{ChangePasswordForm, LoginForm}
import port.primary.webService.restAdapter.controllers.json.AuthJsonSupport._
import utility.Common._
import utility.exceptions.BadRequestParameter

import scalaz.{-\/, \/-}
import scala.util.{Failure, Success, Try}

/**
  * Created by specter8x on 3/18/17.
  */
class AuthController @Inject() (userRepo: UserRepository, userService: UserService)
  extends Controller with Authentication with FormValidator{

  def processLogin: Action[AnyContent] = Action { implicit request =>
    LoginForm.form.bindFromRequest.fold(
      hasErrors => BadRequest("you have to enter email and password"),
      formData => userService.validateUser(formData.email, formData.password) match {
        case \/-(user) =>
          Logger.info("User " + user.email + " login")
          Ok(successResponse).withSession(
            "userId"    -> user.userId.toString,
            "userTime" -> currentMilliseconds.toString
          ).withCookies(
            Cookie("userId", user.userId.toString, httpOnly = false),
            Cookie("username", user.email.toString, httpOnly = false)
          )
        case -\/(e) =>
          Ok(errorResponse(e))
      }
    )
  }

  // process change password
  def processChangePassword() : Action[AnyContent] = isAuthenticated{ implicit request =>
    val result =
      for {
        oldUser <- userRepo.getById(getCurrentUserId)
        formPass <- validateForm(ChangePasswordForm.form)
        matchOldPass <- validateOldPass(formPass.oldPass, oldUser.credential)
        newUser = oldUser.updatePassword(formPass.newPass)
        samePass <- isNotChangePass(oldUser, newUser)
        updateResult <- userRepo.update(newUser)
      } yield {
        updateResult
      }
    result match {
      case Success(_) => Ok("user.change_pass_success").withNewSession.withCookies()
      case Failure(ex) => ex match {
        case _: BadRequestParameter => BadRequest(ex.getMessage)
        case _  =>
          Logger.error("Error when change password for userId " + userSession(request)("userId") + "\n" + ex.getMessage)
          InternalServerError("system.fails")
      }
    }
  }

  private def validateOldPass(oldPass: String, credential: Credential): Try[Boolean] = Try{
    credential.checkPassword(oldPass) match {
      case true => true
      case _ => throw new BadRequestParameter("user.old_password_not_match")
    }
  }

  private def isNotChangePass(oldUser: User, newUser: User): Try[Boolean] = Try{
    if (oldUser.credential == newUser.credential)
      throw new BadRequestParameter("user.old_password_same_new_pass")
    else
      true
  }
}
