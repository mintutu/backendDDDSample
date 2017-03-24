package port.primary.webService.restAdapter.forms

import play.api.data.Form
import play.api.data.Forms._

/**
  * Created by specter8x on 3/21/17.
  */

case class ChangePasswordForm (oldPass: String, newPass: String)

object ChangePasswordForm {

  val form = Form(
    mapping(
      "oldPass" -> nonEmptyText.verifying("min length is 8", _.length >=8),
      "newPass" -> tuple(
        "raw" -> nonEmptyText.verifying("min length is 8", _.length >=8),
        "confirm" -> nonEmptyText.verifying("min length is 8", _.length >=8)
      ).verifying("new password (confirm) must be same new password", newPass => newPass._1 == newPass._2)
    )((oldPass, newPass) => new ChangePasswordForm(oldPass, newPass._1))
    (_ => None))
}
