package controllers

import java.lang.management.ManagementFactory

import akka.util.duration.intToDurationInt
import play.api.libs.concurrent.Promise
import play.api.libs.iteratee.{ Enumerator, Iteratee }
import play.api.mvc.{ Action, Controller, WebSocket }

object Application extends Controller {

  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def statusFeed = WebSocket.using[String] { implicit request =>
    val in = Iteratee.ignore[String]
    val out = Enumerator.fromCallback { () =>
      Promise.timeout(Some(getLoadAverage), 3 seconds)
    }
    (in, out)
  }

  def getLoadAverage = {
    "%1.2f" format
      ManagementFactory.getOperatingSystemMXBean.getSystemLoadAverage()
  }

}