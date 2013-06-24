package models

// #ch05-anorm-model
case class Product(
  id: Long,
  ean: Long,
  name: String,
  description: String)

case class Warehouse(id: Long, name: String)

case class StockItem(
  id: Long,
  productId: Long,
  warehouseId: Long,
  quantity: Long)
// #ch05-anorm-model

object Product {
  // #ch05-anorm-query
  import anorm.SQL
  import anorm.SqlQuery

  val sql: SqlQuery = SQL("select * from products order by name asc")
  // #ch05-anorm-query

  // #ch05-anorm-stream
  import play.api.Play.current
  import play.api.db.DB

  def getAll: List[Product] = DB.withConnection {   //#A


    implicit connection =>  //#B

    sql().map ( row => //#C

      Product(row[Long]("id"), row[Long]("ean"),          //#D
        row[String]("name"), row[String]("description"))  //#D

    ).toList  //#E
  }
  // #ch05-anorm-stream

  // #ch05-anorm-patterns
  def getAllWithPatterns: List[Product] = DB.withConnection {
    implicit connection =>

    import anorm.Row

    sql().collect {
      case Row(Some(id: Long), Some(ean: Long),              //#A
          Some(name: String), Some(description: String)) =>  //#A
        Product(id, ean, name, description)    //#B
    }.toList
  }
  // #ch05-anorm-patterns

  // #ch05-anorm-product-parser
  import anorm.RowParser

  val productParser: RowParser[Product] = {
    import anorm.~
    import anorm.SqlParser._

    long("id") ~ long("ean") ~ str("name") ~ str("description") map {
      case id ~ ean ~ name ~ description =>
        Product(id, ean, name, description)
    }
  }
  // #ch05-anorm-product-parser

  // #ch05-anorm-products-parser
  import anorm.ResultSetParser

  val productsParser: ResultSetParser[List[Product]] = {
     productParser *
  }
  // #ch05-anorm-products-parser

  // #ch05-anorm-parse-products
  def getAllWithParser: List[Product] = DB.withConnection {
    implicit connection =>
    sql.as(productsParser)
  }
  // #ch05-anorm-parse-products

  def findById(id: Long): Option[Product] = {
    DB.withConnection { implicit connection =>
      val sql = SQL("select * from products where id = {id}")
      sql.on("id" -> id).as(productParser.singleOpt)
    }
  }

  // #ch05-anorm-productstockitem-parser

  def productStockItemParser: RowParser[(Product, StockItem)] = {
    import anorm.~
    import anorm.SqlParser._

    productParser ~ StockItem.stockItemParser map (flatten)
  }
  // #ch05-anorm-productstockitem-parser

  // #ch05-anorm-productstockitems
  def getAllProductsWithStockItems: Map[Product, List[StockItem]] = {
    DB.withConnection { implicit connection =>
      val sql = SQL("select p.*, s.* " +                     //#A
        "from products p " +                                 //#A
        "inner join stock_items s on (p.id = s.product_id)") //#A

      val results: List[(Product, StockItem)] =
        sql.as(productStockItemParser *)        //#B

      results.groupBy { _._1 }.mapValues { _.map { _._2 } } //#C
    }
  }
  // #ch05-anorm-productstockitems

  def search(query: String) = DB.withConnection { implicit connection =>
    SQL("""select *
      from products
      where name like '%' || {query} || '%'
      or description like '%' || {query} || '%'""").
      on("query" -> query).as(this.productsParser)
  }

  // #ch05-anorm-insert
  def insert(product: Product): Boolean = {
    DB.withConnection { implicit connection =>
      SQL("""insert
      into products
      values ({id}, {ean}, {name}, {description})""").on( //#A
        "id" -> product.id,                   //#B
        "ean" -> product.ean,                 //#B
        "name" -> product.name,               //#B
        "description" -> product.description  //#B
      ).executeUpdate() == 1  //#C
    }
  }
  // #ch05-anorm-insert

  // #ch05-anorm-update-delete
  def update(product: Product): Boolean = {
    DB.withConnection { implicit connection =>
      SQL("""update products
      set name = {name},
      ean = {ean},
      description = {description}
      where id = {id}
       """).on(
        "id" -> product.id,
        "name" -> product.name,
        "ean" -> product.ean,
        "description" -> product.description).
        executeUpdate() == 1
    }
  }

  def delete(product: Product): Boolean = {
    DB.withConnection { implicit connection =>
      SQL("delete from products where id = {id}").
        on("id" -> product.id).executeUpdate() == 0
    }
  }
  // #ch05-anorm-update-delete
}

object StockItem {
  import anorm.RowParser

  // #ch05-anorm-stockitem-parser
  val stockItemParser: RowParser[StockItem] = {
    import anorm.SqlParser._
    import anorm.~

    long("id") ~ long("product_id") ~
        long("warehouse_id") ~ long("quantity") map {
      case id ~ productId ~ warehouseId ~ quantity =>
        StockItem(id, productId, warehouseId, quantity)
    }
  }
  // #ch05-anorm-stockitem-parser
}
