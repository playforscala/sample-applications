package models

import slick.driver.H2Driver.simple._
import Database.threadLocalSession
import play.api.db.DB
import play.api.Play.current

/**
 * A quantity of some product at a location.
 */
case class StockItem(id: Long, productId: Long, warehouseId: Long, quantity: Long)

/**
 * Slick database mapping.
 */
object StockItems extends Table[StockItem]("stock_items") {
  def id = column[Long]("id", O.PrimaryKey)
  def productId = column[Long]("product_id")
  def warehouseId = column[Long]("warehouse_id")
  def quantity = column[Long]("quantity")
  def product = foreignKey("product_fk", productId, Products)(_.id)
  def warehouse = foreignKey("product_fk", warehouseId, Warehouses)(_.id)
  def * = id ~ productId ~ warehouseId ~ quantity <> (StockItem, StockItem.unapply _)
}

/**
 * A stock item location.
 */
case class Warehouse(id: Long, name: String)

object Warehouses extends Table[Warehouse]("warehouses") {
  def id = column[Long]("id", O.PrimaryKey)
  def name = column[String]("name")
  def * = id ~ name <> (Warehouse, Warehouse.unapply _)
}

