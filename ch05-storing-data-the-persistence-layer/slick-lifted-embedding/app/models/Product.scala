package models

import slick.driver.H2Driver.simple._
import Database.threadLocalSession
import play.api.Logger
import play.api.db.DB

/**
 * Product domain model.
 */
case class Product(
  id: Long,
  ean: Long,
  name: String,
  description: String)


/**
 * Slick database mapping.
 */
object Products extends Table[Product]("products") {
  def id = column[Long]("id", O.PrimaryKey)
  def ean = column[Long]("ean")
  def name = column[String]("name")
  def description = column[String]("description")
  def * = id ~ ean ~ name ~ description <> (Product, Product.unapply _)
}

/**
 * Data access facade.
 * Note: declaring a Product companion object breaks the <> mapping above.
 */
object ProductDatabase {

  /**
   * Delete all database rows.
   * Note that an alternative would be to use slick to run DDL drop and create statements.
   */
  def reset() {
    Database.forDataSource(DB.getDataSource()) withSession {
      // Output database DDL create statements to bootstrap Evolutions file.
      Logger.info(Products.ddl.dropStatements.mkString("/n"))
      Logger.info(Products.ddl.createStatements.mkString("/n"))

      // Delete all rows
      Query(Products).delete
    }
  }

  /**
   * Adds the given product to the database.
   */
  def insert(product: Product): Int = {
    Database.forDataSource(DB.getDataSource()) withSession {
      Products.insert(product)
    }
  }

  /**
   * Returns a list of products from the database.
   */
  def getAll: List[Product] = {
    Database.forDataSource(DB.getDataSource()) withSession {
      Query(Products).list
    }
  }

  def getAllProductsWithStockItems: Map[Product, List[StockItem]] = {
  }
}