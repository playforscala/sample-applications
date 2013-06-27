package controllers

import play.api.mvc._
import models.{Product, Products}
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.Messages

object Application extends Controller {

  val productForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "ean" -> longNumber,
      "name" -> nonEmptyText,
      "description" -> optional(text)
    )(Product.apply)(Product.unapply)
  )

  /**
   * Lists products.
   */
  def index = Action { implicit request =>
    Ok(views.html.index(Products.findAll))
  }

  /**
   * Deletes a product.
   */
  def delete(id: Long) = Action {
    Products.find(id).map { product =>
      Products.delete(id)
      Redirect(routes.Application.index).flashing("success" -> Messages("success.delete", product.name))
    }.getOrElse(NotFound)
  }

  /**
   * Shows product details, for editing.
   */
  def details(ean: Long) = Action { implicit request =>
    Products.findByEan(ean).map { product =>
      Ok(views.html.details(product.id, productForm.fill(product)))
    }.getOrElse {
      Redirect(routes.Application.index).flashing("error" -> Messages("error.notFound", ean))
    }
  }

  /**
   * Inserts a product.
   */
  def insert() = Action { implicit request =>
    productForm.bindFromRequest.fold(
      form => {
        BadRequest(views.html.details(None, form))
      },
      product => {
        Products.insert(product)
        Redirect(routes.Application.index).flashing("success" -> Messages("success.insert", product.name))
      }
    )
  }

  /**
   * Blank form for a new product.
   */
  def blank = Action { implicit request =>
    Ok(views.html.details(None, productForm))
  }

  /**
   * Updates a product.
   */
  def update(id: Long) = Action { implicit request =>
    productForm.bindFromRequest.fold(
      form => {
        Ok(views.html.details(Some(id), form)).flashing("error" -> "Fix the errors!")
      },
      product => {
        Products.update(id, product)
        Redirect(routes.Application.index).flashing("success" -> Messages("success.update", product.name))
      }
    )
  }
}