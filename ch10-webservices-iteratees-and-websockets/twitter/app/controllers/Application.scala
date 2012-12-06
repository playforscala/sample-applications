package controllers

import play.api.mvc._
import play.api.Play
import play.api.Play.current
import play.api.mvc.BodyParsers.parse

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}