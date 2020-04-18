package controllers

import javax.inject._
import play.api.mvc._

/**
 * Minimal controller examples that output text/plain responses.
 */
class Application @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action {
    Ok("Hello world")
  }

  def hello(name: String) = Action {
    Ok("Hello " + name)
  }
}

/**
 * Alternate controller that renders a template. This example is a separate class so the action method name
 * can also be ‘hello’.
 */
class Application2 @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def hello(name: String) = Action {
    Ok(views.html.hello(name))
  }
}
