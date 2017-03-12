package port.primary.webService.restAdapter.controllers.json

import domain.masterData.user.User
import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
  * Created by specter8x on 3/12/17.
  */
object UserJsonSupport extends JsonSupportBase{

  implicit val userWrites = Writes[User] (
    (user: User) => JsObject(Seq(
      "userId" -> JsNumber(user.userId),
      "userName" -> JsString(user.userName),
      "email" -> JsString(user.email),
      "isInactive" -> JsBoolean(user.isInactive)
    ))
  )
  def write(users: Seq[User]): JsValue = Json.toJson(users)
  def write(user: User): JsValue = Json.toJson(user)

}
