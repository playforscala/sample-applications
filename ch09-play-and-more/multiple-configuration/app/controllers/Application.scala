package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current

object Application extends Controller {

  def index = Action {
    val environment = Play.configuration.getString("environment")
    environment map { environment =>
      val config = Play.configuration.getConfig("mail.override").get
      val enabled = config.getBoolean("enabled").getOrElse(false)
      val address = config.getString("address").getOrElse("")
      val addressConfig = if (enabled) address else "not enabled"

      val message = "%s environment: e-mail address override is %s"
      Ok(message format(environment, addressConfig))

    } getOrElse {
      BadRequest("No environment configuration")
    }

  }
}