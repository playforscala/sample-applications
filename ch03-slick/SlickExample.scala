import slick.driver.PostgresDriver.simple._
import Database.threadLocalSession

/**
 * Simple Slick example
 */


// Product table definition
object Product extends Table[(Long, String, String)]("products") {
  def ean = column[Long]("ean", O.PrimaryKey)
  def name = column[String]("name")
  def description = column[String]("description")
  def * = ean ~ name ~ description
}

// Query object.
val products = for {
  product <- Product.sortBy(product => product.name.asc)
} yield (product.ean, product.name, product.description)

// Database session
val url = "jdbc:postgresql://localhost/slick?user=slick&password=slick"
Database.forURL(url, driver = "org.postgresql.Driver") withSession {

  val productList = products.list
  println(productList mkString "\n")
}

