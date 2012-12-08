package models

/**
 * An entry in the product catalogue.
 *
 * @param ean EAN-13 code - a unique product identifier
 * @param name Product name
 * @param description Product description
 */
case class Product(ean: Long, name: String, description: String)

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
  def findAll = this.products.toList.sortBy(_.ean)

  /**
   * The product with the given EAN code.
   */
  def findByEan(ean: Long) = this.products.find(_.ean == ean)

  /**
   * Saves a product to the catalog.
   */
  def save(product: Product) = {
    findByEan(product.ean).map( oldProduct =>
      this.products = this.products - oldProduct + product
    ).getOrElse(
      throw new IllegalArgumentException("Product not found")
    )
  }
}
