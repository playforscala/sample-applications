package controllers

import javax.inject._
import play.api.mvc._
import models.Product

class Products @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  
  def list = Action { implicit request =>

    val products = Product.findAll

    Ok(views.html.products.list(products))
  }
}