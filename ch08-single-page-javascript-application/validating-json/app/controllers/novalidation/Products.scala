package controllers.novalidation

import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.data.validation.ValidationError
import play.api.libs.functional.syntax._
import models.{Contact, Company, Product}

object Products extends Controller {

implicit val companyReads: Reads[Company] = (
  (JsPath \ "name").read[String] and
  (JsPath \ "contact_details").read(
    (
      (JsPath \ "email").readOpt[String] and
      (JsPath \ "fax").readOpt[String] and
      (JsPath \ "phone").readOpt[String]
    )(Contact.apply _))
  )(Company.apply _)

implicit val productReads: Reads[Product] = (
  (JsPath \ "ean").read[Long] and
  (JsPath \ "name").read[String] and
  (JsPath \ "description").readOpt[String] and
  (JsPath \ "pieces").readOpt[Int] and
  (JsPath \ "manufacturer").read[Company] and
  (JsPath \ "tags").read[List[String]] and
  (JsPath \ "active").read[Boolean]
)(Product.apply _)

}