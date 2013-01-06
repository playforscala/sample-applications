package controllers

import models.Cart
import play.api.mvc.RequestHeader

trait WithCart {

  implicit def cart(implicit request: RequestHeader) = {
    // Get a fake cart. In a real app, you'd get it from the session here.
	Cart.demoCart()
  }

}