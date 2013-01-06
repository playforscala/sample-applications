package controllers

import play.api._
import play.api.mvc._
import play.api.data.{ Form, Forms }

object CustomInput extends Controller {
  val datetimeForm = Form(Forms.single("mydatetime" -> Forms.text))
  
  def form() = Action {
    Ok(views.html.custominput.show(datetimeForm))
  }
}