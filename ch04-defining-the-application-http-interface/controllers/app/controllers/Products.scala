package controllers

import play.api.mvc.{Action, Controller}

/**
 * A controller class with four action methods.
 */
object Products extends Controller {

  def list(pageNumber: Int) = Action {   // #1 Show product list
    NotImplemented
  }

  def details(ean: Long) = Action {      // #2 Show product details
    NotImplemented
  }

  def edit(ean: Long) = Action {         // #3 Edit product details
    NotImplemented
  }

  def update(ean: Long) = Action {       // #4 Update product details
    NotImplemented
  }
}


object Products2 extends Controller {

  def list = Action { request =>
    NotImplemented                 // #A
  }

}

object Products3 extends Controller {

  def list(pageNumber: Int) = Action {
    NotImplemented
  }
}