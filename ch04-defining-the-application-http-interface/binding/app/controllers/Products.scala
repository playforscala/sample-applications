package controllers

import play.api.mvc.{Action, Controller}
import play.api.data._
import models.Product

/**
 * Binding syntax examples: this application does not generate any output.
 */
object Products extends Controller {

  def details(ean: Long) = Action {
    NotImplemented
  }

  import play.api.data.Forms._

  val form = Form(
    mapping(
      "ean" -> longNumber,
      "name" -> nonEmptyText,
      "description" -> text
    )(Product.apply)(Product.unapply)
  )
}
