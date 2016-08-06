package controllers

import play.api.mvc._

/**
 * Minimal controller examples that output text/plain responses.
 */
class Application extends Controller {

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
class Application2 extends Controller {

  def hello(name: String) = Action {
    Ok(views.html.hello(name))
  }
}
