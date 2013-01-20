package controllers

import play.api.mvc._
import models.{ProductDatabase, Product}

object Application extends Controller {

  /**
   * Renders the web page, including a table of products.
   */
  def index = Action {
    Ok(views.html.index(ProductDatabase.getAll))
  }

  /**
   * Resets the database, so that there is data to display.
   */
  def resetData = Action {

    ProductDatabase.reset()

    // TODO: replace ID values with generated sequence.
    var products = Set(
      Product(1, 5010255079763L, "Paperclips Large",
        "Large Plain Pack of 1000"),
      Product(2, 5018206244666L, "Giant Paperclips",
        "Giant Plain 51mm 100 pack"),
      Product(3, 5018306332812L, "Paperclip Giant Plain",
        "Giant Plain Pack of 10000"),
      Product(4, 5018306312913L, "No Tear Paper Clip",
        "No Tear Extra Large Pack of 1000"),
      Product(5, 5018206244611L, "Zebra Paperclips",
        "Zebra Length 28mm Assorted 150 Pack")
    )

    products.foreach { product =>
      ProductDatabase.insert(product)
    }

    Redirect(routes.Application.index)
  }

}