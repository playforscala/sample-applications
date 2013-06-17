package com.github.playforscala.barcodes

import play.api.mvc.{Action, Controller}
import util.{Failure, Success}
import play.api.libs.concurrent.Execution.Implicits._


object BarcodesController extends Controller {


  def barcode(ean: Long) = Action {

    Async {
      Barcodes.renderImage(ean) map {
        case Success(image) => Ok(image).as(Barcodes.mimeType)
        case Failure(e) =>
          BadRequest("Couldn’t generate bar code. Error: " + e.getMessage)
      }
    }
  }

}
