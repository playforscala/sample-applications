
package com.github.playforscala.barcodes

import akka.actor.ActorRef
import akka.pattern.ask
import util.{Failure, Success}
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.duration._
import akka.util.Timeout
import scala.language.postfixOps

object Barcodes {
  private[barcodes] var barcodeCache: ActorRef = _

  val mimeType = "image/png"
  val imageResolution = 144

  def renderImage(ean: Long): Future[Array[Byte]] = {
    implicit val timeout = Timeout(20 seconds)

    this.barcodeCache ? RenderImage(ean) flatMap {
      case RenderResult(Success(image)) => Future.successful(image)
      case RenderResult(Failure(e)) => Future.failed(e)
    }
  }
}
