package models

/**
 * Domain model.
 */
case class Product(ean: Long, name: String, description: String)

/**
 * Data access object stub.
 */
object Product {
  def delete(ean: Long) = {}
}
