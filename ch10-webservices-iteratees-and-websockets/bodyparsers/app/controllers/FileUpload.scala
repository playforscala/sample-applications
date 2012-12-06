package controllers

import play.api._
import play.api.mvc._
import play.api.mvc.BodyParsers.parse
import play.api.Play.current

object FileUpload extends Controller {

  def index() = Action {
    Ok(views.html.fileupload.index())
  }

  def file() = Action(parse.multipartFormData) { request =>
    request.body.file("file") map { filePart =>
      val file = filePart.ref.file
      Ok("File uploaded, size %s bytes!" format file.length())
    } getOrElse(BadRequest("File missing!"))
  }

}