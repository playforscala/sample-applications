package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import ManualForm.productForm
import models.Product 

object GeneratedForm extends Controller {
  
  def createForm() = Action {
    Ok(views.html.generatedform.showform(productForm))
  }
  
  def create() = Action { implicit request =>
    productForm.bindFromRequest.fold(
      formWithErrors => Ok(views.html.generatedform.showform(formWithErrors)),
      value => Ok("created: " + value)
    )
  }
  
  def updateForm(ean: Long) = Action {
    Product.findByEan(ean).map { product =>
      val filledForm = productForm.fill(product)
      Ok(views.html.generatedform.showform(filledForm))
    }.getOrElse(NotFound)
  }
}