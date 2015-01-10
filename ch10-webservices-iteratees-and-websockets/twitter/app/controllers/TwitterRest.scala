package controllers

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import models.Tweet
import models.Tweet._
import play.api.Logger
import play.api.Play
import play.api.Play.current
import play.api.cache.{ Cache, Cached }
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.api.mvc.{ Action, Controller }
import play.api.libs.oauth.{ ConsumerKey, OAuthCalculator, RequestToken }

object TwitterRest extends Controller {

  val consumerKey = ConsumerKey(
    key = Play.configuration getString "twitter.consumerkey" getOrElse "",
    secret = Play.configuration getString "twitter.consumersecret" getOrElse "")

  val accessToken = RequestToken(
    token = Play.configuration getString "twitter.accesstoken" getOrElse "",
    secret = Play.configuration getString "twitter.accesstokensecret" getOrElse "")

  def blocking() = Action {
    val results = 3
    val query = """paperclip OR "paper clip""""

    val responsePromise = WS.url("https://api.twitter.com/1.1/search/tweets.json").withQueryString("q" -> query, "rpp" -> results.toString).sign(OAuthCalculator(consumerKey, accessToken)).get
    val response = Await.result(responsePromise, 10.seconds)

    val tweets = Json.parse(response.body).\("statuses").as[Seq[Tweet]]

    // Cheating for the screenshot in the book ;)
    /*
    val tweets = List(
      Tweet("Francisco Canedo", "Why paper clips if you can have staples?"),
      Tweet("Peter Hilton", "Six whiteboards, fifteen thousand sticky notes and a box of paper clips. Ready for Scrum."),
      Tweet("Erik Bakker", "Paper clips. What would we do without the industrial revolution?"))
    */

    Ok(views.html.twitterrest.tweetlist(tweets))
  }

  def nonblocking() = Action.async {
    val results = 3
    val query = "paperclip OR \"paper clip\""

    val responsePromise = WS.url("https://api.twitter.com/1.1/search/tweets.json").withQueryString("q" -> query, "rpp" -> results.toString).sign(OAuthCalculator(consumerKey, accessToken)).get

    responsePromise.map { response =>
      val tweets = Json.parse(response.body).\("statuses").as[Seq[Tweet]]
      Ok(views.html.twitterrest.tweetlist(tweets))
    }
  }

  def cached() = Action.async {
    val results = 3
    val query = "paperclip OR \"paper clip\""

    Cache.getOrElse("tweets", 10) {
      val responsePromise = WS.url("https://api.twitter.com/1.1/search/tweets.json")
        .withQueryString("q" -> query, "rpp" -> results.toString).sign(OAuthCalculator(consumerKey, accessToken)).get

      Logger.info("Requesting tweets from Twitter")

      responsePromise.map { response =>
        val tweets = Json.parse(response.body).\("statuses").as[Seq[Tweet]]
        Ok(views.html.twitterrest.tweetlist(tweets))
      }
    }
  }

  def cached2() = Cached("tweets2", 10) {
    Action.async {
      Logger.info("Requesting tweets from Twitter")

      val results = 3
      val query = "paperclip OR \"paper clip\""

      val responsePromise = WS.url("https://api.twitter.com/1.1/search/tweets.json").withQueryString("q" -> query, "rpp" -> results.toString).sign(OAuthCalculator(consumerKey, accessToken)).get

      responsePromise map { response =>
        val tweets = Json.parse(response.body).\("statuses").as[Seq[Tweet]]
        Ok(views.html.twitterrest.tweetlist(tweets))
      }
    }
  }

}