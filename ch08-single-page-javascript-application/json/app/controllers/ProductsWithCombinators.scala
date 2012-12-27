package controllers

import play.api.mvc._
import play.api.mvc.Controller
import models.Product

/**
 * Alternative controller that uses the combinator-based Writes.
 */
object ProductsWithCombinators extends Controller {

  import play.api.libs.json._
  import play.api.libs.functional.syntax._

  val productWrites: Writes[Product] = (
    (JsPath \ "ean").write[Long] and
    (JsPath \ "name").write[String] and
    (JsPath \ "description").write[String]
  )(unlift(Product.unapply))

  /**
   * Returns details of the given product.
   */
  def details(ean: Long) = Action {
    Product.findByEan(ean).map { product =>
      Ok(Json.toJson(product)(productWrites))
    }.getOrElse(NotFound)
  }

}
