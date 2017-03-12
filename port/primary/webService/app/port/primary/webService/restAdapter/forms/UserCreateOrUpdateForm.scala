package port.primary.webService.restAdapter.forms

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._

/**
  * Created by specter8x on 3/12/17.
  */
case class UserCreateOrUpdateForm (userName: String, email: String){
}

object UserCreateOrUpdateForm {
  implicit val form = Form(
    mapping(
      "username" -> nonEmptyText,
      "email" -> nonEmptyText
    )(UserCreateOrUpdateForm.apply)(UserCreateOrUpdateForm.unapply)
  )
}
