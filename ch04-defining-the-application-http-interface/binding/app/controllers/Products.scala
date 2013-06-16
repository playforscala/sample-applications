package controllers

import play.api.mvc.{Action, Controller}
import play.api.data._
import models.Product

object Products extends Controller {

  def index = Action {
    Redirect("/product/5010255079763")
  }

  def details(ean: Long) = Action {
    // Process request…
    Ok("EAN = " + ean)
  }

  import play.api.data.Forms._

  val productForm = Form(
    mapping(
      "ean" -> longNumber,
      "name" -> nonEmptyText,
      "description" -> text
    )(Product.apply)(Product.unapply)
  )
  
  /**
   * Creates a `Product` instance from HTTP request parameters.
   * e.g. http://localhost:9000/form?ean=5010255079761&name=Name&description=Desc
   */
  def form = Action { implicit request =>
    val product = productForm.bindFromRequest.get
    Ok("product = " + product.toString)
  }
}
