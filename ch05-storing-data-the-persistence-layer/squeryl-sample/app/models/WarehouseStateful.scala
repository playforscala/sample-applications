package models

import org.squeryl.KeyedEntity

case class WarehouseStateful(name: String) extends KeyedEntity[Long] {
  override val id = 0L

  lazy val stockItems = DatabaseStateless.warehouseToStockItems.leftStateful(this)
}
