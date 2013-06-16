package controllers

import models.Product
import play.api.mvc.{Action, Controller}

object Products extends Controller {

  def index = Action {
    Ok("Reverse routing syntax examples; this application does not produce any output.")
  }

  def list = Action {
    NotImplemented      // #A
  }

  def delete(ean: Long) = Action {
    Product.delete(ean)
    Redirect("/proudcts")            // #A fail at run-time because of a typo in the URL
  }                                  // #A

  def list(flag: Boolean) = Action {
    NotImplemented
  }
}

object Products2 extends Controller {

  def delete(ean: Long) = Action {
    Product.delete(ean)
    Redirect(routes.Products.list())   // #A Redirect to the list() action
  }
}
