package controllers

import play.api.mvc.{Action, Controller}
import play.api.libs.concurrent.Akka
import concurrent.{ExecutionContext, Future}

object Dashboard extends Controller {

  /**
   * Suspends an HTTP request while waiting for asynchronous processing.
   */
  def backlog(warehouse: String) = Action {

    import play.api.Play.current
    val backlog: Future[String] = Akka.future {
      models.Order.backlog(warehouse)
    }

    import ExecutionContext.Implicits.global
    Async {
      backlog.map(value => Ok(value))
    }
  }
}
