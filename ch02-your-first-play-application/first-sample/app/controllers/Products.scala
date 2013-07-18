package controllers

import play.api.mvc._
import play.api.mvc.Controller
import models.Product
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.Messages

/**
 * Controller for products HTTP interface.
 */
object Products extends Controller {

  /**
   * Returns true if the given EAN’s checksum is correct.
   *
   * @param ean the EAN to check
   * @return true if the checksum is correct, false otherwise
   */
  private def eanCheck(ean: Long) = {
    def sumDigits(digits: IndexedSeq[(Char, Int)]): Int = {
      digits.map { _._1 }.map { _.toInt }.sum
    }

    val (singles, triples) = ean.toString.reverse.zipWithIndex.partition {
      _._2 % 2 == 0
    }

    (sumDigits(singles) + sumDigits(triples) * 3) % 10 == 0
  }

  /**
   * Generates a [[play.api.data.Form]][[[models.Product]]] with a variable
   * constraint for the “EAN” field.
   *
   * @param error the error message key
   * @param constraint the constraint to be added to the “EAN” field
   * @return the new form
   */
  private def makeProductForm(error: String, constraint: (Long) => Boolean) = Form(
    mapping(
      "ean" -> longNumber.verifying("validation.ean.checksum", eanCheck _).verifying(error, constraint),
      "name" -> nonEmptyText,
      "description" -> nonEmptyText
    )(Product.apply)(Product.unapply)
  )

  /**
   * Returns true if no [[models.Product]] exists with the given EAN.
   *
   * @param ean the EAN to look for
   * @return true if the EAN is unique, false otherwise
   */
  private def isUniqueEan(ean: Long): Boolean = Product.findByEan(ean).isEmpty

  /**
   * [[play.api.data.Form]] for parsing new-product data from requests.
   */
  private val productForm = makeProductForm("validation.ean.duplicate", isUniqueEan(_))

  /**
   * Generates a [[play.api.data.Form]][[[models.Product]]] with a duplicate-EAN
   * check for update.
   *
   * @param ean the previous value of the EAN of the to-be-updated product
   * @return the new form
   */
  private def updateProductForm(ean: Long) =
    makeProductForm("validation.ean.duplicate", { newEan =>
      newEan == ean || isUniqueEan(newEan)
    })

  /**
   * Displays a products list.
   */
  def list = Action { implicit request =>
    Ok(views.html.products.list(Product.findAll))
  }

  /**
   * Displays a blank form for adding a new product.
   */
  def newProduct = Action { implicit request =>
    val form = if (flash.get("error").isDefined) {
      val errorForm = productForm.bind(flash.data)

        errorForm
    } else
      productForm

    Ok(views.html.products.editProduct(form))
  }

  /**
   * Displays the product with the given EAN code.
   */
  def show(ean: Long) = Action { implicit request =>
    Product.findByEan(ean).map { product =>
      Ok(views.html.products.details(product))
    }.getOrElse(NotFound)
  }

  /**
   * Saves a new product’s details.
   */
  def save = Action { implicit request =>
    val newProductForm = productForm.bindFromRequest()

    newProductForm.fold(
      hasErrors = { form =>
        Redirect(routes.Products.newProduct()).flashing(Flash(form.data) +
          ("error" -> Messages("validation.errors")))
      },
      success = { newProduct =>
        Product.add(newProduct)
        val successMessage = ("success" -> Messages("products.new.success", newProduct.name))
        Redirect(routes.Products.show(newProduct.ean)).flashing(successMessage)
      }
    )
  }

  /**
   * Displays a form for editing product details.
   */
  def edit(ean: Long) = Action { implicit request =>
    val form = if (flash.get("error").isDefined)
      updateProductForm(ean).bind(flash.data)
    else
      updateProductForm(ean).fill(Product.findByEan(ean).get)

    Ok(views.html.products.editProduct(form, Some(ean)))
  }

  /**
   * Saves changes to a product’s details.
   */
  def update(ean: Long) = Action { implicit request =>
    if (Product.findByEan(ean).isEmpty)
      NotFound
    else {
      val updatedProductForm = updateProductForm(ean).bindFromRequest()

      updatedProductForm.fold(
        hasErrors = { form =>
          Redirect(routes.Products.edit(ean)).flashing(Flash(form.data) +
            ("error" -> Messages("validation.errors")))
        },
        success = { updatedProduct =>
          Product.remove(Product.findByEan(ean).get)
          Product.add(updatedProduct)
          val successMessage = "success" -> Messages("products.update.success", updatedProduct.name)
          Redirect(routes.Products.show(updatedProduct.ean)).flashing(successMessage)
        }
      )
    }
  }
}
