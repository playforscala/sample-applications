package controllers

import scala.concurrent.duration.DurationInt

import akka.actor.{ Props, actorRef2Scala }
import akka.pattern.ask
import akka.util.Timeout
import models.Tweet
import models.Tweet.tweetWrites
import play.api.{ Logger, Play }
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.libs.functional.syntax.{ functionalCanBuildApplicative, toFunctionalBuilderOps }
import play.api.libs.iteratee.{ Enumerator, Iteratee }
import play.api.libs.json.{ JsPath, JsValue, Json }
import play.api.libs.oauth.{ ConsumerKey, OAuthCalculator, RequestToken }
import play.api.libs.ws.{ ResponseHeaders, WS }
import play.api.mvc.{ Action, Controller, WebSocket }
import streaming.{ Broadcast, BroadcasterActor, Subscribe }

object TwitterStreaming extends Controller {
  implicit val timeout = Timeout(5 seconds)

  val streamBroadcaster = Akka.system.actorOf(Props[BroadcasterActor])

  val consumerKey = ConsumerKey(
    key = Play.configuration getString "twitter.consumerkey" getOrElse "",
    secret = Play.configuration getString "twitter.consumersecret" getOrElse "")

  val accessToken = RequestToken(
    token = Play.configuration getString "twitter.accesstoken" getOrElse "",
    secret = Play.configuration getString "twitter.accesstokensecret" getOrElse "")

  val twitterStream = WS.url("https://stream.twitter.com/1/statuses/sample.json").sign(OAuthCalculator(consumerKey, accessToken)).get(responseProcessor)

  def index() = Action { implicit request =>
    val error = if (consumerKey.key.isEmpty || consumerKey.secret.isEmpty || accessToken.token.isEmpty || accessToken.secret.isEmpty) {
      Some("Please configure all Twitter credentials in application.conf!")
    } else None

    Ok(views.html.streaming.index(error))
  }

  def stream() = WebSocket.async[JsValue] { request =>
    val future = streamBroadcaster ? Subscribe
    future.mapTo[(Iteratee[JsValue, _], Enumerator[JsValue])]
  }

  // Note that this is a different reads from the one in the Tweet object,
  // since the streaming API gives slightly different results.
  val streamTweetReads = (
    (JsPath \ "user" \ "name").read[String] ~
    (JsPath \ "text").read[String])(Tweet.apply _)

  /**
   * Exercise: rewrite this iteratee into one that doesn't use mutable state.
   */
  def responseProcessor(headers: ResponseHeaders): Iteratee[Array[Byte], Unit] = {
    if (headers.status == 200) {
      var tweets = 0
      var messages = 0
      Iteratee.foreach[Array[Byte]] { chunk =>
        messages += 1
        val chunkString = new String(chunk, "UTF-8")
        val tweetOption = Json.parse(chunkString).asOpt[Tweet](streamTweetReads)
        tweetOption foreach { tweet =>
          streamBroadcaster ! Broadcast(Json.toJson(tweet))
          tweets += 1
        }

        if (messages % 1000 == 0) {
          Logger.info("Received %s messages, of which %s tweets" format (messages, tweets))
        }
      }
    } else {
      Logger.warn("Failed to connect to Twitter!")
      Iteratee.foreach(chunk => Logger.warn(new String(chunk, "UTF-8")))
    }
  }

}