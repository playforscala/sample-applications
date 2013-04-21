import play.api.mvc.RequestHeader
import play.api.{GlobalSettings, Logger}

object Global extends GlobalSettings {
  override def onRouteRequest(request: RequestHeader) = {
    Logger.info("got route request for [%s]" format request)
    super.onRouteRequest(request)
  }

  override def onRequestCompletion(request: RequestHeader) {
    Logger.info("completed request for [%s]" format request)
    super.onRequestCompletion(request)
  }
}
