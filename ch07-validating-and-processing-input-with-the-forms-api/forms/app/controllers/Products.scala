package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

object Products extends Controller {

  def save() = Action { request =>

    val data = Map(
      "name" -> "Box of paper clips",
      "ean" -> "1234567890123",
      "pieces" -> "300")

    val productForm = Form(
      Forms.tuple(
        "name" -> Forms.text,
        "ean" -> Forms.text,
        "pieces" -> Forms.number))

    val boundForm = productForm.bind(data)

    val success = boundForm.fold(_ => false, _ => true)

    boundForm.fold(
      formWithErrors => BadRequest,
      value => {
        doSomethingWithValue(value)
        Ok
      })

  }

  def doSomethingWithValue(a: Any) = {}

  case class User(
    username: String,
    realname: Option[String],
    email: String)

  val userForm = Form(
    mapping(
      "username" -> nonEmptyText(8),
      "realname" -> optional(text),
      "email" -> email)(User.apply)(User.unapply))

  def createUser() = Action { implicit request =>

    val c = userForm.bindFromRequest.fold(
      formWithErrors => Forbidden,
      user => Ok("User created!"))
    c
  }
  
  def test() = Action {
    Results.Forbidden
  }
}