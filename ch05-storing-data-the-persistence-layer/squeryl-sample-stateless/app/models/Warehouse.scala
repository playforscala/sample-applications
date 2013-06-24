package models

import org.squeryl.KeyedEntity

case class Warehouse(name: String) extends KeyedEntity[Long] {
  override val id = 0L
}
