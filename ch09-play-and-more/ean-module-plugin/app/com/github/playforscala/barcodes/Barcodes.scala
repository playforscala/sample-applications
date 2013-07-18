
package com.github.playforscala.barcodes

import akka.actor.ActorRef
import akka.pattern.ask
import util.Try
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.duration._
import akka.util.Timeout

object Barcodes {
  var barcodeCache: ActorRef = _

  val mimeType = "image/png"
  val imageResolution = 144

  def renderImage(ean: Long): Future[Try[Array[Byte]]] = {
    implicit val timeout = Timeout(20 seconds)

    barcodeCache ? RenderImage(ean) map {
      case RenderResult(result) => result
    }
  }
}
