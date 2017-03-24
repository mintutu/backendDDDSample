package port.primary.webService.restAdapter.controllers.json

/**
  * Created by specter8x on 3/18/17.
  */
object AuthJsonSupport extends JsonSupportBase{
  def successResponse = result2Json(true)
  def errorResponse(msg: String) = result2Json(false, msg)
}
