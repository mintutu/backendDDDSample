package restAdapter.controllers.commons

import play.api.data.Form
import play.api.mvc._
import utility.exceptions.BadRequestParameter

import scala.util.{Failure, Success, Try}

/**
  * Created by specter8x on 3/9/17.
  */
trait FormValidator extends Controller with Authentication{

  def withValidateForm[T](form: Form[T])(f: T=> Result): Action[AnyContent] = isAuthenticated {
    implicit request => {
      form.bindFromRequest.fold(
        hasError => BadRequest("Invalid input: " + hasError.errors.mkString(" ")),
        formData => f(formData)
      )
    }
  }

  def withValidatedFormSession[T](form: Form[T])(f: T => Map[String, String] => Result): Action[AnyContent] = isAuthenticated {
    implicit request => {
      form.bindFromRequest.fold(
        hasErrors => BadRequest("Invalid input: " + hasErrors.errors.mkString(" ")),
        formData => f(formData)(userSession(request))
      )
    }
  }

  def validateForm[T](form: Form[T])(implicit request: Request[AnyContent]): Try[T] = {
      form.bindFromRequest().fold(
        hasError => Failure(new BadRequestParameter("Invalid input: " + hasError.errors.mkString(" "))),
        formData => Success(formData)
      )
  }
}
