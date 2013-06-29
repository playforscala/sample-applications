package models

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB.withSession
import play.api.Play.current
import scala.slick.lifted.Query

/**
 * An entry in the product catalogue.
 *
 * @param ean EAN-13 code - a unique product identifier
 * @param name Product name
 * @param description Product description
 */
case class Product(id: Option[Long], ean: Long, name: String, description: Option[String])

object Products extends Table[Product]("products") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def ean = column[Long]("ean")
  def name = column[String]("name")
  def description = column[Option[String]]("description")
  def * = id.? ~ ean ~ name ~ description <> (Product, Product.unapply _)
  def forInsert = ean ~ name ~ description <> (
    t => Product(None, t._1, t._2, t._3),
    (p: Product) => Some((p.ean, p.name, p.description)))

  /**
   * Deletes a product.
   */
  def delete(id: Long) {
    withSession { implicit session =>
      Products.where(_.id === id).delete
    }
  }

  def find(id: Long): Option[Product] = withSession { implicit session =>
    Query(Products).filter(_.id === id).list.headOption
  }

  /**
   * Returns the product with the given EAN code.
   */
  def findByEan(ean: Long): Option[Product] = withSession { implicit session =>
    Query(Products).filter(_.ean === ean).list.headOption
  }

  /**
   * Returns all products sorted by EAN code.
   */
  def findAll: List[Product] = withSession { implicit session =>
    Query(Products).sortBy(_.ean).list
  }

  /**
   * Inserts the given product.
   */
  def insert(product: Product) {
    withSession { implicit session =>
      Products.forInsert.insert(product)
    }
  }

  /**
   * Updates the given product.
   */
  def update(id: Long, product: Product) {
    withSession { implicit session =>
      Products.where(_.id === id).update(product)
    }
  }
}
