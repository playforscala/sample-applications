package com.github.playforscala.barcodes

import play.api.mvc.{Action, Controller}
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider
import org.krysalis.barcode4j.impl.upcean.EAN13Bean
import util.{Failure, Success, Try}


object Barcodes extends Controller {

  val ImageResolution = 144

  def barcode(ean: Long) = Action {

    val MimeType = "image/png"
    Try(ean13BarCode(ean, MimeType)) match {
      case Success(imageData) => Ok(imageData).as(MimeType)
      case Failure(e) => BadRequest("Couldn’t generate bar code. Error: " + e.getMessage)
    }
  }

  def ean13BarCode(ean: Long, mimeType: String): Array[Byte] = {

    import java.io.ByteArrayOutputStream
    import java.awt.image.BufferedImage

    val output = new ByteArrayOutputStream
    val canvas =
      new BitmapCanvasProvider(output, mimeType, ImageResolution,
        BufferedImage.TYPE_BYTE_BINARY, false, 0)

    val barCode = new EAN13Bean
    barCode.generateBarcode(canvas, String valueOf ean)
    canvas.finish()

    output.toByteArray
  }
}
