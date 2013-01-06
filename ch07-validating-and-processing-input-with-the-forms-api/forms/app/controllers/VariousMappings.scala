package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._


object VariousMappings extends Controller {
  
  case class Person(name: String, age: Option[Int])
  val personMapping = mapping(
    "name" -> nonEmptyText,
    "age" -> optional(number)
  )(Person.apply)(Person.unapply)
  
  val contactMapping = tuple(
      "name" -> text,
      "email" -> email
    )
  
  val variousForm = Form(tuple(
    "optionalText" -> optional(text),
    "tags" -> list(text),
    "mainContact" -> contactMapping,
    "technicalContact" -> optional(contactMapping)))
  
  def form() = Action {
    Ok(views.html.variousmappings.form(variousForm))
  }
  
  def process() = Action { implicit request =>
    variousForm.bindFromRequest.fold(
      formWithErrors => Ok(views.html.variousmappings.form(formWithErrors)),
      value => Ok("Created: %s" format value)
    )
  }
  
  val contactsForm = Form(tuple(
    "main_contact" -> contactMapping,
    "technical_contact" -> contactMapping,
    "administrative_contact" -> contactMapping))
    
  def showContactsForm() = Action {
    Ok(views.html.variousmappings.contactsForm(contactsForm))
  }
  
  case class Contact(name: String, email: String)
  val appointmentMapping = tuple(
  "location" -> text,
  "start" -> tuple(
    "date" -> date,
    "time" -> text),
  "attendees" -> list(mapping(
    "name" -> text,
    "email" -> email)(Contact.apply)(Contact.unapply)))
    
  val typedMapping = tuple[String, Int, String](
    "name" -> text,
    "age" -> number,
    "email" -> email
  )
}