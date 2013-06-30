package controllers

import play.api._
import play.api.mvc._

// We extend the 'WithCart' trait, so we have an implicit conversion from RequestHeader to Cart
object Application extends Controller with WithCart {

  def index = Action { implicit request =>
    // The index template takes an implicit Cart, which is not available.
    // However, the WithCart trait has an implicit conversion from
    // RequestHeader to Cart, and we do have an implicit RequestHeader
    // here, because `request` is a Request, which extends RequestHeader.
    Ok(views.html.index())
  }

  def contact = Action { implicit request =>
    Ok(views.html.contact())
  }

}