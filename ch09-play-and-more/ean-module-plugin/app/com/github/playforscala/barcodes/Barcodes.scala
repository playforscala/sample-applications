
package com.github.playforscala.barcodes

import akka.actor.ActorRef
import akka.pattern.ask
import util.Failure
import scala.concurrent.Future
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.duration._
import akka.util.Timeout
import scala.language.postfixOps

object Barcodes {
  private[barcodes] var barcodeCache: ActorRef = _

  val mimeType = "image/png"
  val imageResolution = 144

  def renderImage(ean: Long): Future[Image] = {
    implicit val timeout = Timeout(20 seconds)

    this.barcodeCache ? RenderImage(ean) map { _ match {
      case image: Image => image
      case badMessage =>
        val message = "Got bad message from barcodeCache Actor: [%s]".format(badMessage)
        Logger.error(message)
        Image(Failure(new IllegalArgumentException(message)))
    }}
  }
}
