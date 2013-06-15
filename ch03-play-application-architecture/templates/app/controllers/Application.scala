package controllers

import play.api.mvc._

/**
 * Controller that renders example templates.
 */
object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  /**
   * Renders a minimal HTML template with no parameters.
   */
  def minimal = Action {
    Ok(views.html.minimal())
  }

  /**
   * Renders a template with a String title parameter.
   */
  def title = Action {
    Ok(views.html.title("New Arrivals"))
  }

}