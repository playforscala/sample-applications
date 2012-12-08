package controllers

import play.api.mvc._
import play.api.mvc.Controller
import models.Product
import play.api.libs.json._

/**
 * Controller for products HTTP interface.
 */
object Products extends Controller {

  /**
   * Returns an array of products’ EAN codes.
   */
  def list = Action {
    val productCodes = Product.findAll.map(_.ean)
    Ok(Json.toJson(productCodes))
  }

  /**
   * Formats a Product instance as JSON.
   */
  implicit object ProductWrites extends Writes[Product] {
    def writes(p: Product) = Json.toJson(
      Map(
        "ean" -> Json.toJson(p.ean),
        "name" -> Json.toJson(p.name),
        "description" -> Json.toJson(p.description)
      )
    )
  }

  /**
   * Returns details of the given product.
   */
  def details(ean: Long) = Action {
    Product.findByEan(ean).map { product =>
      Ok(Json.toJson(product))
    }.getOrElse(NotFound)
  }

  /**
   * Parses a JSON object
   */
  implicit object ProductReads extends Reads[Product] {
    def reads(json: JsValue) = Product(
      (json \ "ean").as[Long],
      (json \ "name").as[String],
      (json \ "description").as[String]
    )
  }

  /**
   * Saves a product
   */
  def save(ean: Long) = Action(parse.json) { request =>
    val productJson = request.body
    val product = productJson.as[Product]
    try {
      Product.save(product)
      Ok("Saved")
    }
    catch {
      case e:IllegalArgumentException => BadRequest("Product not found")
    }
  }

}
