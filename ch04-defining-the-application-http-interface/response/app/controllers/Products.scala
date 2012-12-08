package controllers

import play.api.mvc.{Action, Controller}
import models.Product


object Products extends Controller {

  def version = Action {
    Ok("Version 2.0")
  }

  def index = Action {
    Ok(views.html.index())
  }

  def json = Action {
    import play.api.libs.json.Json

    val success = Map("status" -> "success")
    val json = Json.toJson(success)
    Ok(json)
  }

  def jsonInline = Action {
    import play.api.libs.json.Json

    Ok(Json.toJson(Map("status" -> "success")))
  }

  def xml = Action {
    Ok(<status>success</status>)
  }

  def list = Action {
    new Status(500)
  }

  def details(ean: Long) = Action {
    NotImplemented
  }

  def create = Action {
    val product = Product(5010255079763L, "", "")

    val url = routes.Products.details(product.ean).url   // #A

    Status(CREATED).withHeaders(LOCATION -> url)         // #B
  }

  def contentType = Action {
    val json = """{ "status": "success" }"""
    Ok(json).withHeaders(CONTENT_TYPE -> "application/json")
  }

  def contentTypeAs = Action {
    Ok("""{ "status": "success" }""").as("application/json")
  }

  def contentTypeAsConstant = Action {
    Ok("""{ "status": "success" }""").as(JSON)
  }

  def session = Action { request =>
    val search = request.session.get("search.previous")

    if (search.isDefined) {
      val results = "search = " + search.get
      Ok(results).withSession(
        request.session - "search.previous"
      )
    }
    else {
      val query = "kittens"
      val results = "search UNDEFINED"
      Ok(results).withSession(
        request.session + ("search.previous" -> query)
      )
    }

  }

  def flash = Action { request =>
    if (request.flash.isEmpty) {
      Ok("UNDEFINED")
    }
    else {
      val message = request.flash("info")
      Ok(message)
    }
  }

  def delete = Action {
    Redirect(routes.Products.flash()).flashing(
      "info" -> "Product deleted!"
    )
  }


  def ean13Barcode(ean: Long, mimeType: String): Array[Byte] = {

    import java.io.ByteArrayOutputStream
    import java.awt.image.BufferedImage
    import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider
    import org.krysalis.barcode4j.impl.upcean.EAN13Bean

    val BarcodeResolution = 72

    val output: ByteArrayOutputStream = new ByteArrayOutputStream
    val canvas: BitmapCanvasProvider =
      new BitmapCanvasProvider(output, mimeType, BarcodeResolution,
        BufferedImage.TYPE_BYTE_BINARY, false, 0)

      val barcode = new EAN13Bean()
      barcode.generateBarcode(canvas, String valueOf ean)
      canvas.finish

      output.toByteArray
  }

  def barcode(ean: Long) = Action {

    import java.lang.IllegalArgumentException

    val MimeType = "image/png"        // #A

    try {
      val imageData: Array[Byte] =    // #B
        ean13Barcode(ean, MimeType)

      Ok(imageData).as(MimeType)      // #C
    }
    catch {                           // #D

      case e: IllegalArgumentException =>
      BadRequest("Could not generate bar code. Error: " + e.getMessage)
    }
  }
}