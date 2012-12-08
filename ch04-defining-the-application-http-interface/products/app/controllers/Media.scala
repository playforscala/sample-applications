package controllers

import play.api.mvc.{Action, Controller}

object Media extends Controller {

    def photo(file: String) = Action {
      Ok("file: " + file)
    }
}