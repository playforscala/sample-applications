package models

import java.text.SimpleDateFormat
import java.util.Date

/**
 * A product that is stored in a warehouse.
 */
case class Product(ean: Long, description: String)

/**
 * A request to prepare an order by ‘picking’ an order line (a
 * quantity of a particular product) from the given warehouse location.
 * A list of preparation requests is called a ‘pick list’.
 */
case class Preparation(orderNumber: Long, product: Product,
  quantity: Int, location: String)

/**
 * Pick list data access object stub (test data).
 */
object PickList {

  def find(warehouse: String) : List[Preparation] = {
    val p = Product(5010255079763L, "Large paper clips 1000 pack")
    List(
      Preparation(3141592, p, 200, "Aisle 42 bin 7"),
      Preparation(6535897, p, 500, "Aisle 42 bin 7"),
      Preparation(93, p, 100, "Aisle 42 bin 7")
    )
  }
}

/**
 * Warehouse data access object stub.
 */
object Warehouse {

  def find() = {
    List("W123", "W456")
  }
}

object Order {

  def backlog(warehouse: String): String = {
    Thread.sleep(5000L)
    new SimpleDateFormat("mmss").format(new Date())
  }
}