package controllers

import play.api._
import play.api.mvc._
import models.PickList
import templates.Html
import java.util.Date
import scala.concurrent.{ExecutionContext, future}

object PickLists extends Controller {

  /**
   * Renders a pick list synchronously in the usual way.
   */
  def preview(warehouse: String) = Action {
    val pickList = PickList.find(warehouse)
    val timestamp = new java.util.Date
    Ok(views.html.pickList(warehouse, pickList, timestamp))
  }

  /**
   * Starts a job to generate a pick list.
   */
  def sendAsync(warehouse: String) = Action {
    import ExecutionContext.Implicits.global
    future {
      val pickList = PickList.find(warehouse)
      send(views.html.pickList(warehouse, pickList, new Date))
    }
    Redirect(routes.PickLists.index())
  }

  /**
   * Renders the home page.
   */
  def index = Action {
    Ok(views.html.index())
  }

  /**
   * Stub for ‘sending’ the pick list as an HTML document, e.g. by e-mail.
   */
  private def send(html: Html) {
    Logger.info(html.body)
    // Send pick list…
  }

}