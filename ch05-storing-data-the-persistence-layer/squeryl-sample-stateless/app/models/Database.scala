package models

import org.squeryl.PrimitiveTypeMode._
import org.squeryl.Schema

object Database extends Schema {
	val productsTable = table[Product]("products")
  val warehousesTable = table[Warehouse]("warehouses")
  val stockItemsTable = table[StockItem]("stockitems")

	on(this.productsTable) { p =>
		declare (
      p.id is(autoIncremented),
      p.ean is(unique)
    )
	}

  on(this.warehousesTable) { w =>
    declare (
      w.id is(autoIncremented),
      w.name is(unique)
    )
  }

  on(this.stockItemsTable) { s =>
    declare (
      s.id is(autoIncremented),
      columns(s.product, s.location) are(unique)
    )
  }
}
