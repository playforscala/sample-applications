package controllers

import play.api.mvc.{Action, Controller}
import models.Product
import play.api.libs.json.Json

object Products1 extends Controller {

  def list = Action {
    val productCodes = Product.findAll.map(_.ean)
    Ok(Json.toJson(productCodes))
  }
}
