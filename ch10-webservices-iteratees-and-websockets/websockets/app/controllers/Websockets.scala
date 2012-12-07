package controllers

import play.api._
import play.api.libs.iteratee.Iteratee
import play.api.mvc._
import play.api.libs.iteratee.Enumerator
import java.lang.management.ManagementFactory
import play.api.libs.concurrent.Promise
import akka.util.duration._

object WebSockets extends Controller {
  
  def statusPage() = Action { implicit request => 
    Ok(views.html.websockets.statusPage(request))
  }

  def statusFeed() = WebSocket.using[String] { implicit request =>
    def getLoadAverage = {
      "%1.2f" format 
        ManagementFactory.getOperatingSystemMXBean.getSystemLoadAverage()
    }
    
    val in = Iteratee.ignore[String]
    val out = Enumerator.fromCallback { () =>
      Promise.timeout(Some(getLoadAverage), 3 seconds)
    }
    
    (in, out)
  }
} 