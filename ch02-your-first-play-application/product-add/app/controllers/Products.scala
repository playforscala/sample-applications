package controllers

import javax.inject._
import play.api.mvc._
import models.Product
import play.api.data.Form
import play.api.data.Forms.{mapping, longNumber, nonEmptyText}
import play.api.i18n.Messages

class Products @Inject()(cc: ControllerComponents)(c: play.api.Configuration)
  extends AbstractController(cc)
  with play.api.i18n.I18nSupport {

  private val productForm: Form[Product] = Form(
    mapping(
      "ean" -> longNumber.verifying(
        "validation.ean.duplicate", Product.findByEan(_).isEmpty),
      "name" -> nonEmptyText,
      "description" -> nonEmptyText
    )(Product.apply)(Product.unapply)
  )

  val foo = productForm.copy()

  def list = Action { implicit request =>

    val products = Product.findAll

    implicit lazy val config = c
    implicit lazy val lang = request.lang
    Ok(views.html.products.list(products))
  }

  def show(ean: Long) = Action { implicit request =>

    Product.findByEan(ean).map { product =>

      implicit lazy val config = c
      implicit lazy val lang = request.lang
      Ok(views.html.products.details(product))

    }.getOrElse(NotFound)
  }

  def newProduct = Action { implicit request =>
    val form = if (request.flash.get("error").isDefined)
      productForm.bind(request.flash.data)
    else
      productForm

    implicit lazy val config = c
    implicit lazy val lang = request.lang
    Ok(views.html.products.editProduct(form))
  }

  def save = Action { implicit request =>
    val newProductForm = productForm.bindFromRequest()

    newProductForm.fold(
      hasErrors = { form =>
        Redirect(routes.Products.newProduct()).
          flashing(Flash(form.data) +
          ("error" -> Messages("validation.errors")))
      },
      success = { newProduct =>
        Product.add(newProduct)
        val message = Messages("products.new.success", newProduct.name)
        Redirect(routes.Products.show(newProduct.ean)).
          flashing("success" -> message)
      }
    )
  }
}
