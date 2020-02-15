package controllers

import javax.inject._
import play.api.mvc._
import models.Product

class Products @Inject()(cc: ControllerComponents)(c: play.api.Configuration)
  extends AbstractController(cc)
  with play.api.i18n.I18nSupport {
  
  def list = Action { implicit request =>

    val products = Product.findAll

    implicit lazy val config = c
    Ok(views.html.products.list(products))
  }
}