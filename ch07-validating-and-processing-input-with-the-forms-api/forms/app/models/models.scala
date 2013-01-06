package models

case class Product(
  ean: Long,
  name: String,
  description: String,
  pieces: Int,
  active: Boolean)
  
object Product {
  def findByEan(ean: Long) = None
}