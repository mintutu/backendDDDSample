package port.primary.webService.restAdapter.forms

import play.api.data.Form
import play.api.data.Forms._

/**
  * Created by specter8x on 3/18/17.
  */
case class LoginForm (email: String, password: String)

object LoginForm {
  val form = Form(
    mapping(
      "email" -> email,
      "password" -> nonEmptyText
    )(LoginForm.apply)(LoginForm.unapply)
  )
}
