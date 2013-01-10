package models

import play.api.libs.json._
import play.api.libs.json.util._
import play.api.libs.json.Reads._
import play.api.libs.json.Writes._
import play.api.libs.functional.syntax._

case class Tweet(from: String, text: String)

object Tweet {

  implicit val tweetReads = (
      (JsPath \ "from_user_name").read[String] ~
      (JsPath \ "text").read[String])(Tweet.apply _)

  implicit val tweetWrites = (
    (JsPath \ "from").write[String] ~
    (JsPath \ "text").write[String])(unlift(Tweet.unapply))
}