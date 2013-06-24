package models

import org.squeryl.PrimitiveTypeMode._
import org.squeryl.Schema

object DatabaseStateful extends Schema {
  val productsTable = table[ProductStateful]("products")
  val warehousesTable = table[WarehouseStateful]("warehouses")
  val stockItemsTable = table[StockItemStateful]("stockitems")

  val productToStockItems =
    oneToManyRelation(productsTable, stockItemsTable).via ( (p,s) =>
      p.id === s.product
    )

  val warehouseToStockItems =
    oneToManyRelation(warehousesTable, stockItemsTable).via ( (w,s) =>
      w.id === s.location
    )
}
