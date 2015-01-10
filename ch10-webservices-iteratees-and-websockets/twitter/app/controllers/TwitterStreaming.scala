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
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.functional.syntax.{ functionalCanBuildApplicative, toFunctionalBuilderOps }
import play.api.libs.iteratee.{ Enumerator, Iteratee }
import play.api.libs.json.{ JsPath, JsValue, Json }
import play.api.libs.oauth.{ ConsumerKey, OAuthCalculator, RequestToken }
import play.api.libs.ws.{ ResponseHeaders, WS }
import play.api.mvc.{ Action, Controller, WebSocket }
import streaming.{ Broadcast, BroadcasterActor, Subscribe }
import scala.util.control.NonFatal
import scala.annotation.tailrec

object TwitterStreaming extends Controller {
  implicit val timeout = Timeout(5.seconds)

  val streamBroadcaster = Akka.system.actorOf(Props[BroadcasterActor])

  val consumerKey = ConsumerKey(
    key = Play.configuration getString "twitter.consumerkey" getOrElse "",
    secret = Play.configuration getString "twitter.consumersecret" getOrElse "")

  val accessToken = RequestToken(
    token = Play.configuration getString "twitter.accesstoken" getOrElse "",
    secret = Play.configuration getString "twitter.accesstokensecret" getOrElse "")

  val twitterStream = WS.url("https://stream.twitter.com/1.1/statuses/sample.json").sign(OAuthCalculator(consumerKey, accessToken)).get(responseProcessor)

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

  // Split the byte array on tweet boundaries, and return full tweets and remaining
  // bytes
  private def getTweets(bytes: Array[Byte]): (List[Array[Byte]], Array[Byte]) = {
    val delimiter = "\r\n".getBytes

    @tailrec
    def loop(tweets: List[Array[Byte]], remainder: Array[Byte]): (List[Array[Byte]], Array[Byte]) =
      remainder.indexOfSlice(delimiter) match {
        case -1 => tweets -> remainder
        case index =>
          val tweetBytes = remainder.take(index)
          val newRemainder = remainder.drop(index + delimiter.length)
          loop(tweets :+ tweetBytes, newRemainder)
      }

    loop(Nil, bytes)
  }
  /**
   * Exercise: rewrite this iteratee into one that doesn't use mutable state.
   */
  def responseProcessor(headers: ResponseHeaders): Iteratee[Array[Byte], _] = {

    if (headers.status == 200) {
      var tweetCount = 0
      var messages = 0
      Iteratee.fold[Array[Byte], Array[Byte]](Array()){ (remainder, newChunk) =>

        val (tweets, newRemainder) = getTweets(remainder ++ newChunk)

        tweets.map { tweetBytes =>
          messages += 1
          val tweetString = new String(tweetBytes, "UTF-8")

          val tweetOption = Json.parse(tweetString).asOpt[Tweet](streamTweetReads)
          tweetOption foreach { tweet =>
            streamBroadcaster ! Broadcast(Json.toJson(tweet))
            tweetCount += 1
          }

          if (messages % 1000 == 0) {
            Logger.info("Received %s messages, of which %s tweets" format (messages, tweetCount))
          }
        }

        newRemainder
      }
    } else {
      Logger.warn("Failed to connect to Twitter!")
      Iteratee.foreach(chunk => Logger.warn(new String(chunk, "UTF-8")))
    }
  }

}