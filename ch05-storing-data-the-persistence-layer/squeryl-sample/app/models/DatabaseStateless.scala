package models

import org.squeryl.PrimitiveTypeMode._
import org.squeryl.Schema

object DatabaseStateless extends Schema {
  val productsTable = table[ProductStateless]("products")
  val warehousesTable = table[WarehouseStateless]("warehouses")
  val stockItemsTable = table[StockItemStateless]("stockitems")

  val productToStockItems =
    oneToManyRelation(productsTable, stockItemsTable).via ( (p,s) =>
      p.id === s.product
    )

  val warehouseToStockItems =
    oneToManyRelation(warehousesTable, stockItemsTable).via ( (w,s) =>
      w.id === s.location
    )
}
