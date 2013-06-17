package models

/**
 * An entry in the product catalogue.
 *
 * @param ean EAN-13 code - a unique product identifier
 * @param name Product name
 * @param description Product description
 */
case class Product(ean: Long, name: String, description: String) {
  override def toString = "%s - %s".format(ean, name)
}

/**
 * Products data access
 */
object Product {
  
  var products = Set(
    Product(5010255079763L, "Paperclips Large", "Large Plain Pack of 1000"),
    Product(5018206244666L, "Giant Paperclips", "Giant Plain 51mm 100 pack"),
    Product(5018306332812L, "Paperclip Giant Plain", "Giant Plain Pack of 10000"),
    Product(5018306312913L, "No Tear Paper Clip", "No Tear Extra Large Pack of 1000"),
    Product(5018206244611L, "Zebra Paperclips", "Zebra Length 28mm Assorted 150 Pack")
  )

  /**
   * Products sorted by EAN code.
   */
  def findAll = products.toList.sortBy(_.ean)

  /**
   * The product with the given EAN code.
   */
  def findByEan(ean: Long) = products.find(_.ean == ean)

  /**
   * Products whose name matches the given query.
   */
  def findByName(query: String) = products.filter(_.name.contains(query))

  /**
   * Deletes a product from the catalog.
   */
  def remove(product: Product) = {
    val oldProducts = products
    products = products - product
    oldProducts.contains(product)
  }

  /**
   * Adds a product to the catalog.
   */
  def add(product: Product) {
    products = products + product
  }
}
