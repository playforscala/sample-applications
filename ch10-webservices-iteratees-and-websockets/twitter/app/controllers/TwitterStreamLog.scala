package controllers

import models.Tweet
import play.api.Play
import play.api.Play.current
import play.api.libs.iteratee.Iteratee
import play.api.libs.json.{ JsValue, Reads }
import play.api.libs.oauth.{ ConsumerKey, OAuthCalculator, RequestToken }
import play.api.libs.ws.WS
import play.api.mvc.{ Action, Controller }

object TwitterStreamLog extends Controller {

  val consumerKey = ConsumerKey(
    key = Play.configuration getString "twitter.consumerkey" getOrElse "",
    secret = Play.configuration getString "twitter.consumersecret" getOrElse "")

  val accessToken = RequestToken(
    token = Play.configuration getString "twitter.accesstoken" getOrElse "",
    secret = Play.configuration getString "twitter.tokensecret" getOrElse "")

  object StreamTweetReads extends Reads[Tweet] {
    def reads(json: JsValue): Tweet = {
      Tweet((json \ "user" \ "name").as[String],
        (json \ "text").as[String])
    }
  }

  val loggingIteratee = Iteratee.foreach[Array[Byte]] { chunk =>
    val chunkString = new String(chunk, "UTF-8")
    println(chunkString)
  }

  def streamAndLog() = Action {
    val twitterStream = WS.url("https://stream.twitter.com/1/statuses/sample.json")
      .sign(OAuthCalculator(consumerKey, accessToken))
      .get(_ => loggingIteratee)
    Ok("Check the console output!")
  }

}