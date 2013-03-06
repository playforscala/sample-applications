package com.github.playforscala.barcodes

import akka.actor.Actor
import concurrent._
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider
import org.krysalis.barcode4j.impl.upcean.EAN13Bean
import scala.util.Try
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._

class BarcodeCache extends Actor {
  private var imageCache = Map[Long, Future[Array[Byte]]]()

  def receive = {
    case RenderImage(ean) => {
      Logger.info("Got request for image")
      val futureImage = this.imageCache.get(ean) match {
        case Some(futureImage) => {
          Logger.info("Cache hit")
          futureImage
        }
        case None => {
          Logger.info("Cache miss")
          val futureImage = future { ean13BarCode(ean, "image/png") }
          this.imageCache += (ean -> futureImage)
          futureImage
        }
      }

      val currentSender = sender

      futureImage.onComplete {
        Logger.info("Sending image")
        currentSender ! Image(_)
      }
    }
  }

  private def ean13BarCode(ean: Long, mimeType: String): Array[Byte] = {

    import java.io.ByteArrayOutputStream
    import java.awt.image.BufferedImage

    var output: ByteArrayOutputStream = new ByteArrayOutputStream
    var canvas: BitmapCanvasProvider =
      new BitmapCanvasProvider(output, mimeType, Barcodes.imageResolution,
        BufferedImage.TYPE_BYTE_BINARY, false, 0)

    val barCode = new EAN13Bean()
    barCode.generateBarcode(canvas, String valueOf ean)
    canvas.finish

    output.toByteArray
  }
}

case class RenderImage(ean: Long)
case class Image(image: Try[Array[Byte]])

