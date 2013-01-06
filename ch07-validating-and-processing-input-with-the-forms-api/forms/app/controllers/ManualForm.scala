package controllers

import models.Product
import play.api._
import play.api.mvc._
import play.api.data.{ Form }
import play.api.data.Forms._

object ManualForm extends Controller {

  def createForm() = Action {
    Ok(views.html.manualform.showform())
  }

  val productForm = Form(mapping(
    "ean" -> longNumber.verifying("This product already exists!", Product.findByEan(_).isEmpty),
    "name" -> nonEmptyText,
    "description" -> text,
    "pieces" -> number,
    "active" -> boolean)(Product.apply)(Product.unapply).verifying(
      "Product can not be active if the description is empty", product =>
        !product.active || product.description.nonEmpty))

  def create() = Action { implicit request =>
    productForm.bindFromRequest.fold(
      formWithErrors => Forbidden("Oh noes, invalid submission!"),
      value => Ok("created: " + value))
  }
}