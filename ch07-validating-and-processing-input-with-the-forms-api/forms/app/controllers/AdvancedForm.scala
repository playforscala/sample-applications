package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Product
import org.joda.time.LocalDate
import play.api.data.format.Formatter
import scala.util.control.Exception
import org.joda.time.LocalDateTime
import scala.util.Try

/**
 * Advanced form demonstrating a custom mapping
 *
 * This form contains a custom mapping for `LocalDate`.
 */
object AdvancedForm extends Controller {

  val localDateMapping1 = text.transform(
    (dateString: String) => LocalDate.parse(dateString),
    (localDate: LocalDate) => localDate.toString)

  implicit val localDateFormatter = new Formatter[LocalDate] {
    def bind(key: String, data: Map[String, String]) =
      data.get(key) map { value =>
        Try {
          Right(LocalDate.parse(value))
        } getOrElse Left(Seq(FormError(key, "error.date", Nil)))
      } getOrElse Left(Seq(FormError(key, "error.required", Nil)))

    def unbind(key: String, ld: LocalDate) = Map(key -> ld.toString)

    override val format = Some(("date.format", Nil))
  }

  val localDateMapping2 = of[LocalDate]
  val localDateMapping3 = of(localDateFormatter)

  val localDateForm = Form(single(
    "introductionDate" -> localDateMapping2))

  def createLocalDateForm() = Action {
    Ok(views.html.advancedform.showLocalDateForm(localDateForm))
  }

  def createLocalDate() = Action { implicit request =>
    localDateForm.bindFromRequest.fold(
      formWithErrors => Ok(views.html.advancedform.showLocalDateForm(formWithErrors)),
      value => Ok("created: " + value))
  }

}