package views

import views.html.helper.FieldConstructor

object ViewHelpers {

  implicit val fields = FieldConstructor(views.html.fieldConstructor.f)
}