package controllers

import play.api.mvc.{Action, Cookie, Controller}


case class Product(ean: Long, name: String, description: String) {
}

object Product {
  def delete(ean: Long) = {}
}

/**
 * Controller action samples.
 */
object Products extends Controller {

  def alias(alias: String) = Action {
    Ok("alias: " + alias)
  }

  // Action aliases.
  def path = alias _
  def query = alias _


  // #ch4-response-body-binary
  def binary = Action {
//    import play.api.Play.current
//    val source = Source.fromFile(Play.getFile("public/logo.png"))
//    val data = source.takeWhile(_ != -1).map(_.toByte).toArray
//    source.close()

//    val is = new FileInputStream(Play.getFile("public/logo.png"))
//    val data = continually(is.read).takeWhile(_ != -1).map(_.toByte).toArray

//    Binary(data)
//    Ok(Play.getFile("public/logo.png").length() + " bytes")
    NotImplemented
  }
  // #ch4-response-body-binary

  def list = Action {
    NotImplemented // #A
  }

  def details(ean: Long) = Action {
    // #2 Show product details
    Ok("ean: " + ean)
  }

  
  def cookie = Action { request =>
    val query = "kittens"

    request.cookies.get("search.previous").map { cookie =>
      Ok(cookie.value)
    }.getOrElse {
      Ok("UNDEFINED").withCookies(
        Cookie("search.previous", query)
      )
    }
  }
  
  def create2 = Action {
    val product = Product(5010255079763L, "", "")
    import controllers.routes

    import play.api.http.HeaderNames
    import play.api.http
    import play.api.mvc.Results

    val url = routes.Products.details(product.ean).url // #A

    val result = Results.Status(http.Status.CREATED)
    result.withHeaders(HeaderNames.LOCATION -> url) // #B
  }

}
