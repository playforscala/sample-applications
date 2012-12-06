package controllers

import amazon.AmazonBodyParsers
import play.api.Play
import play.api.Play.current
import play.api.mvc.{ Action, Controller }

object AmazonS3Upload extends Controller {

  def index() = Action {

    val maybeError = if (!credentialsConfigured)
      Some("Please configure aws.key and aws.secret in application.conf or uploading will fail!")
    else None

    Ok(views.html.amazons3upload.index(maybeError))
  }

  val bucket = Play.configuration.getString("s3.bucket").getOrElse("play-for-scala-sample-bucket")
  val filename = "testfile.dat"

  def file() = Action(AmazonBodyParsers.S3Upload(bucket, filename)) { request =>
    Ok("Your file has been uploaded to Amazon, with filename '%s'" format request.body)
  }

  private def credentialsConfigured = Play.configuration.getString("aws.key").getOrElse("").nonEmpty

}