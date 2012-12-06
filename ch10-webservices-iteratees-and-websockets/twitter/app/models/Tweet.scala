package models

import play.api.libs.json.{ Format, Json, JsObject, JsValue }

case class Tweet(from: String, text: String)

object Tweet {
  
  implicit object TweetFormats extends Format[Tweet] {
  
    def reads(json: JsValue): Tweet = Tweet(
      (json \ "from_user_name").as[String],
      (json \ "text").as[String])
    
    def writes(tweet: Tweet): JsValue = JsObject(List(
      "from" -> Json.toJson(tweet.from),
      "text" -> Json.toJson(tweet.text)))
  }
}