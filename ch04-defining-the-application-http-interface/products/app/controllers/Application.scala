package controllers

import play.api.mvc.{Action, Controller}

object Application extends Controller {

  def home = Action {
    Ok(views.html.index())
  }
}
