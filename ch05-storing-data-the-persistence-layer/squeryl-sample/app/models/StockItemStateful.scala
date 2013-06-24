package models

import org.squeryl.KeyedEntity

case class StockItemStateful(productId: Long, location: Long, quantity: Long) extends KeyedEntity[Long] {
  override val id = 0L

  lazy val product = DatabaseStateless.productToStockItems.rightStateful(this)
  lazy val warehouse = DatabaseStateless.warehouseToStockItems.rightStateful(this)
}
