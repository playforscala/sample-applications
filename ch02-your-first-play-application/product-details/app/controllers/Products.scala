package controllers

import javax.inject._
import play.api.mvc._
import models.Product

class Products @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  
  def list = Action { implicit request =>

    val products = Product.findAll

    Ok(views.html.products.list(products))
  }

  def show(ean: Long) = Action { implicit request =>

    Product.findByEan(ean).map { product =>

      Ok(views.html.products.details(product))

    }.getOrElse(NotFound)
  }
}
