package models

import scala.math.BigDecimal.double2bigDecimal

case class Product(
  id: Long,
  name: String,
  price: BigDecimal,
  description: String)

object ProductDAO {
  def list: Seq[Product] = List(
    Product(123, "400 small paperclips", 4.95, "A box of 400 small paperclips"),
    Product(124, "150 big paperclips", 5.95, "A box of 150 big paperclips"),
    Product(233, "Blue ballpoint", 1.75, "Blue ballpoint pen"))
}