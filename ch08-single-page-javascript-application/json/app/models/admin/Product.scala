package models.admin


/**
 * Domain model.
 */
case class Product(
  ean: Long,
  name: String,
  description: String,
  price: BigDecimal
)

object Product {

  /**
   * Alternative JSON formatter that includes the price.
   */
  import play.api.libs.json._
  import play.api.libs.functional.syntax._

  val adminProductWrites: Writes[Product] = (
    (JsPath \ "ean").write[Long] and
    (JsPath \ "name").write[String] and
    (JsPath \ "description").write[String] and
    (JsPath \ "price").write[BigDecimal]
  )(unlift(Product.unapply))

}
