package models

import slick.driver.H2Driver.simple._
import Database.threadLocalSession
import play.api.db.DB

/**
 * A quantity of some product at a location.
 */
case class StockItem(id: Long, product: Product, warehouse: Warehouse, quantity: Long)

/**
 * Slick database mapping.
 */
object StockItems extends Table[StockItem]("stock_items") {
  def id = column[Long]("id", O.PrimaryKey)
  def productId = column[Long]("product_id")
  def warehouseId = column[Long]("warehouse_id")
  def quantity = column[Long]("quantity")
  def * = id ~ product ~ warehouse ~ quantity <> (StockItem, StockItem.unapply _)
  def product = foreignKey("product_fk", productId, Product)(_.id)
  def warehouse = foreignKey("product_fk", warehouseId, Warehouse)(_.id)
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

