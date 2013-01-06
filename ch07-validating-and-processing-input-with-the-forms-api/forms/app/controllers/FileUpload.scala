package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._

object FileUpload extends Controller{

  def showManualUploadForm() = Action {
    Ok(views.html.fileupload.manualform())
  }

  def showUploadForm() = Action {
    val dummyForm = Form(ignored("dummy"))
    Ok(views.html.fileupload.uploadform(dummyForm))
  }

  def upload() = Action(parse.multipartFormData) { request =>
    request.body.file("image").map { file =>
      Ok("Retrieved file %s" format file.filename)
    }.getOrElse(BadRequest("File missing!"))
  }

  def upload2() = Action(parse.multipartFormData) { implicit request =>
    val form = Form(tuple(
      "description" -> text(minLength = 10),
      "image" -> ignored(request.body.file("image")).verifying("File missing", _.isDefined)
    ))

    form.bindFromRequest.fold(
      formWithErrors => {
        Ok(views.html.fileupload.uploadform(formWithErrors))
      },
      value => Ok("Found file: %s with description %s" format(value._2.get.filename, value._1))
    )
  }
}