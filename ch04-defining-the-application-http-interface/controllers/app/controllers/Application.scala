package controllers

import play.api.mvc.{Action, Controller}


object Application extends Controller {

  def home = Action {
    Ok("<h1>Controller actions</h1>" +
      "<p>This application only demonstrates action method declarations " +
      "and does not generate any output, other than this message.</p>" +
      "<p>See <tt>app/controllers/Products.scala</tt> for the sample code.</p>").as("text/html")
  }
}
