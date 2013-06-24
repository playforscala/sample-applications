package models

import org.squeryl.KeyedEntity

case class StockItem(product: Long, location: Long, quantity: Long) extends KeyedEntity[Long] {
  override val id = 0L
}
