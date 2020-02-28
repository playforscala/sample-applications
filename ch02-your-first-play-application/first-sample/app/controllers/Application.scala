package controllers

import play.api._
import play.api.mvc._

import javax.inject.Inject
import play.api.Configuration

/**
 * Main application controller.
 */
class Application @Inject()(cc: ControllerComponents)
  extends AbstractController(cc) {

  /**
   * Redirect to the product list.
   */
  def index = Action { // implicit request =>
    Redirect(routes.Products.list())
  }
}
