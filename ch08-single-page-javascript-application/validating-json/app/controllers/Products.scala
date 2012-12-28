package controllers

import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.data.validation.ValidationError
import play.api.libs.functional.syntax._
import models.{Contact, Company, Product}

object Products extends Controller {

  implicit object JsPathWrites extends Writes[JsPath] {
    def writes(p: JsPath) = JsString(p.toString)
  }

  implicit object ValidationErrorWrites extends Writes[ValidationError] {
    def writes(e: ValidationError) = Json.toJson(e.message)
  }

  implicit object JsonValidateInvalidWrites extends Writes[Tuple2[JsPath, Seq[ValidationError]]] {
    def writes(t: Tuple2[JsPath, Seq[ValidationError]]) = Json.obj(
      "path" -> Json.toJson(t._1),
      "errors" -> Json.toJson(t._2)
    )
  }

  implicit val contactReads: Reads[Contact] = (
    (JsPath \ "email").readOpt[String](email) and
    (JsPath \ "fax").readOpt[String] and
    (JsPath \ "phone").readOpt[String]
  )(Contact.apply _)

  implicit val companyReads: Reads[Company] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "contact_details").read[Contact]
  )(Company.apply _)

  implicit val productReads: Reads[Product] = (
    (JsPath \ "ean").read[Long] and
    (JsPath \ "name").read[String](minLength[String](5)) and
    (JsPath \ "description").read[String] and
    (JsPath \ "pieces").readOpt[Int] and
    (JsPath \ "manufacturer").read[Company] and
    (JsPath \ "tags").read[List[String]] and
    (JsPath \ "active").read[Boolean]
  )(Product.apply _)

  def validate = Action(parse.json) { implicit request =>
    val json = request.body
    json.validate[Product].fold(
      valid = ( product => Ok("Valid: ‘%s’" format product.name) ),
      invalid = ( errors => BadRequest(Json.toJson(errors)) )
    )
  }
  
}