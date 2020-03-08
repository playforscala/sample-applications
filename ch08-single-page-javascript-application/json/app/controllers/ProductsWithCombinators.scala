package controllers

import javax.inject._
import play.api.mvc._
import models.Product

/**
 * Alternative controller that uses the combinator-based Writes.
 */
class ProductsWithCombinators @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

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
