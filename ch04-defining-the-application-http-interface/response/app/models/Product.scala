package models

/**
 * Domain model.
 */
case class Product(ean: Long, name: String, description: String)

/**
 * Data access object stub.
 */
object Product {
  def all = List(Product(5010255079763L, "Paper clips", "Large plain 1000 pack"))
}
