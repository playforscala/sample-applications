package controllers

import play.api._
import play.api.mvc._

/**
 * Configuration API—programmatic access: demonstrates reading configuration variables.
 */
object Application extends Controller {
  
  def index = Action {

    // Using the Play API to retrieve the current application’s configuration

    import play.api.Play.current                                     // #A
    current.configuration.getString("db.default.url").map {
      databaseUrl => Logger.info(databaseUrl)                        // #B
    }

    // How to check a Boolean configuration property.
    current.configuration.getBoolean("db.default.logStatements").foreach {
      if (_) Logger.info("Logging SQL statements...")
    }

    // Accessing a sub-configuration
    current.configuration.getConfig("db.default").map {                 // #A
      databaseConfiguration =>
      databaseConfiguration.getString("driver").map(Logger.info(_))
      databaseConfiguration.getString("url").map(Logger.info(_))
    }

    Ok(views.html.index("Your new application is ready."))
  }
  
}