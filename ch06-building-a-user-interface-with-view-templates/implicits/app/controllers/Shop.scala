package controllers

import play.api._
import play.api.mvc._
import models._

object Shop extends Controller with WithCart {

  def catalog() = Action { implicit request =>
    val products = ProductDAO.list
    Ok(views.html.products.catalog(products))
  }

}