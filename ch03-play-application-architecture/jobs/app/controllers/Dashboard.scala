package controllers

import play.api.mvc.{Action, Controller}
import play.api.libs.concurrent.{Promise, Akka}

object Dashboard extends Controller {

  /**
   * Suspends an HTTP request while waiting for asynchronous processing.
   */
  def backlog(warehouse: String) = Action {

    import play.api.Play.current
    val backlog: Promise[String] = Akka.future {
      models.Order.backlog(warehouse)
    }

    Async {
      backlog.map(value => Ok(value))
    }
  }
}
