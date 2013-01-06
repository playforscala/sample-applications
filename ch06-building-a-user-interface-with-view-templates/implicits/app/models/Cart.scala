package models

case class Cart(products: Map[Int, Product]) {
  def productCount = products.keys.foldLeft(0)(_ + _)
}

object Cart {
  def demoCart() = {
    Cart(Map(
      3 -> ProductDAO.list(0),
      1 -> ProductDAO.list(1)))
  }
}