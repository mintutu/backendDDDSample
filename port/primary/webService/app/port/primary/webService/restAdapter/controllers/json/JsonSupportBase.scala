package port.primary.webService.restAdapter.controllers.json

import play.api.libs.json.{JsBoolean, JsObject, JsString, Json}

/**
  * Created by specter8x on 3/12/17.
  */
trait JsonSupportBase {

  def result2Json(status: Boolean, msg: String = "", list: Option[JsObject] = None): JsObject = {
    var result = Json.obj()
    result +=("message", JsString(msg))
    result +=("success", JsBoolean(status))

    if (list.isDefined) {
      result +=("data", list.get)
    }
    result
  }
}
