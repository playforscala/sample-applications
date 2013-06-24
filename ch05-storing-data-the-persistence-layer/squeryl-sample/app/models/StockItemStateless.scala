package models

import org.squeryl.KeyedEntity

case class StockItemStateless(productId: Long, location: Long, quantity: Long) extends KeyedEntity[Long] {
  override val id = 0L

  lazy val product = DatabaseStateless.productToStockItems.right(this)
  lazy val warehouse = DatabaseStateless.warehouseToStockItems.right(this)
}
