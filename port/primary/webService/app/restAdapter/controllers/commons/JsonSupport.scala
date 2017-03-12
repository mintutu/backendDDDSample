package restAdapter.controllers.commons

import play.api.libs.json.{JsNull, JsObject, JsString, JsValue}

trait JsonSupport {
  def errorJson(code: String, message: String): JsObject = JsObject(Seq(
    "code" -> JsString(code),
    "message" -> JsString(message),
    "data" -> JsNull
  ))

  def successJson(data: JsValue = JsNull, message: String = "Success"): JsObject = JsObject(Seq(
    "code" -> JsString("Success"),
    "message" -> JsString(message),
    "data" -> data
  ))
}