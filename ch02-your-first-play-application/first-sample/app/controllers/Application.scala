package controllers

import play.api._
import play.api.mvc._

/**
 * Main application controller.
 */
class Application extends Controller {

  /**
   * Redirect to the product list.
   */
  def index = Action { implicit request =>
    Redirect(routes.Products.list())
  }
}
