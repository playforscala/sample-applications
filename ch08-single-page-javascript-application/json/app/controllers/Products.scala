package controllers

import play.api.mvc._
import play.api.mvc.Controller
import models.Product
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.Logger


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
    def writes(p: Product) = Json.obj(
      "ean" -> Json.toJson(p.ean),
      "name" -> Json.toJson(p.name),
      "description" -> Json.toJson(p.description)
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
  implicit val productReads: Reads[Product] = (
    (JsPath \ "ean").read[Long] and
    (JsPath \ "name").read[String] and
    (JsPath \ "description").read[String]
  )(Product.apply _)

  /**
   * Saves a product
   */
  def save(ean: Long) = Action(parse.json) { request =>
    Logger.info("start")
    try {
      val productJson = request.body
      val product = productJson.as[Product]
      Product.save(product)
      Ok("Saved")
    }
    catch {
      case e:IllegalArgumentException => BadRequest("Product not found")
      case e:Exception => {
        Logger.info("exception = %s" format e)
        BadRequest("Invalid EAN")
      }
    }
  }

}
