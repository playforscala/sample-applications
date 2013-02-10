package controllers

import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.data.validation.ValidationError
import play.api.libs.functional.syntax._
import models.{Contact, Company, Product}

object Products extends Controller {

  // Define serialisation for JSON validation error messages.

  implicit val JsPathWrites = Writes[JsPath](p => JsString(p.toString))

  implicit val ValidationErrorWrites =
    Writes[ValidationError](e => JsString(e.message))

  implicit val jsonValidateErrorWrites = (
    (JsPath \ "path").write[JsPath] and
    (JsPath \ "errors").write[Seq[ValidationError]]
    tupled
  )

  // Define JSON parsers for domain model.

  val contactReads: Reads[Contact] = (
    (JsPath \ "email").readNullable[String](email) and
    (JsPath \ "fax").readNullable[String](minLength[String](10)) and
    (JsPath \ "phone").readNullable[String](minLength[String](10))
  )(Contact.apply _)

  implicit val companyReads: Reads[Company] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "contact_details").read(
      (
        (JsPath \ "email").readNullable[String](email) and
        (JsPath \ "fax").readNullable[String](minLength[String](10)) and
        (JsPath \ "phone").readNullable[String](minLength[String](10))
      )(Contact.apply _))
    )(Company.apply _)

  implicit val productReads: Reads[Product] = (
    (JsPath \ "ean").read[Long] and
    (JsPath \ "name").read[String](minLength[String](5)) and
    (JsPath \ "description").readNullable[String] and
    (JsPath \ "pieces").readNullable[Int] and
    (JsPath \ "manufacturer").read[Company] and
    (JsPath \ "tags").read[List[String]] and
    (JsPath \ "active").read[Boolean]
  )(Product.apply _)

  /**
   * Validates a JSON representation of a Product.
   */
  def save = Action(parse.json) { implicit request =>
    val json = request.body
    json.validate[Product].fold(
      valid = { product =>
        Product.save(product)
        Ok("Saved")
      },
      invalid = {
        errors => BadRequest(Json.toJson(errors))
//        errors => BadRequest(JsError.toFlatJson(errors))
      }
    )
  }
  
}