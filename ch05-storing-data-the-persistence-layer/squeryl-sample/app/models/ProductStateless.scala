package models

import org.squeryl.PrimitiveTypeMode._
import org.squeryl.KeyedEntity

/**
 * An entry in the product catalogue.
 *
 * @param ean EAN-13 code - a unique product identifier
 * @param name Product name
 * @param description Product description
 */
case class ProductStateless(ean: Long, name: String, description: String) extends KeyedEntity[Long] {
  override val id = 0L
  override def toString = "%s - %s".format(ean, name)

  lazy val stockItems = DatabaseStateless.productToStockItems.left(this)
}

/**
 * Products data access
 */
object ProductStateless {
  import Database.{productsTable, stockItemsTable}

  /**
   * Query that finds all products
   */
  def allQ = from(productsTable) ( product =>
    select(product) orderBy(product.ean desc)
  )

  /**
   * Products sorted by EAN code.
   */
  def findAll = inTransaction {
    allQ.toList
  }

  def productsInWarehouse(warehouse: Warehouse) = {
    join(productsTable, stockItemsTable) ( (product, stockItem) =>
      where(stockItem.location === warehouse.id)
      select(product)
      on(stockItem.product === product.id)
    )
  }

  def productsInWarehouseByName(name: String, warehouse: Warehouse) = {
    from(productsInWarehouse(warehouse)) ( product =>
      where(product.name like name)
      select(product)
    )
  }

  /**
   * The product with the given EAN code.
   */
  def findByEan(ean: Long) = inTransaction {
    from(productsTable) ( p =>
      where(p.ean === ean)
      select(p)
    ).headOption
  }

  /**
   * Products whose name matches the given query.
   */
  def findByName(query: String) = inTransaction {
    from(productsTable) ( p =>
      where(p.name like ("%" + query + "%"))
      select(p)
    ).toList
  }

  /**
   * Deletes a product from the catalog.
   */
  def remove(product: Product) = inTransaction {
    productsTable.delete(product.id)
  }

  /**
   * Adds a product to the catalog.
   */
  def insert(product: Product) = inTransaction {
    productsTable.insert(product.copy())
  }

  /**
   * Updates a product in the catalg.
   */
  def update(product: Product) { inTransaction {
    productsTable.update(product)
  }}

  def getStockItems(product: ProductStateless) = inTransaction {
    product.stockItems.toList
  }

  def getLargeStockQ(product: ProductStateless, quantity: Long) =
    from(product.stockItems) ( s =>
      where(s.quantity gt quantity)
      select(s)
    )
}
