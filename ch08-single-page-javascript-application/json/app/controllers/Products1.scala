package controllers

import javax.inject._
import play.api.mvc._
import models.Product
import play.api.libs.json.Json

class Products1 @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def list = Action {
    val productCodes = Product.findAll.map(_.ean)
    Ok(Json.toJson(productCodes))
  }
}
