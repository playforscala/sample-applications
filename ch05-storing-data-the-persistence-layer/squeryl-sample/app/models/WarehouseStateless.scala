package models

import org.squeryl.KeyedEntity

case class WarehouseStateless(name: String) extends KeyedEntity[Long] {
  override val id = 0L

  lazy val stockItems = DatabaseStateless.warehouseToStockItems.left(this)
}
