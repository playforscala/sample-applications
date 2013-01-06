package controllers

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

import models.Tweet
import models.Tweet._
import play.api.Logger
import play.api.Play.current
import play.api.cache.{ Cache, Cached }
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.api.mvc.{ Action, Controller }

object TwitterRest extends Controller {

  def blocking() = Action {
    val results = 3
    val query = """paperclip OR "paper clip""""

    val responsePromise = WS.url("http://search.twitter.com/search.json").withQueryString("q" -> query, "rpp" -> results.toString).get
    val response = Await.result(responsePromise, 10 seconds)

    val tweets = Json.parse(response.body).\("results").as[Seq[Tweet]]

    // Cheating for the screenshot in the book ;)
    /*
    val tweets = List(
      Tweet("Francisco Canedo", "Why paper clips if you can have staples?"),
      Tweet("Peter Hilton", "Six whiteboards, fifteen thousand sticky notes and a box of paper clips. Ready for Scrum."),
      Tweet("Erik Bakker", "Paper clips. What would we do without the industrial revolution?"))
    */

    Ok(views.html.twitterrest.tweetlist(tweets))
  }

  def nonblocking() = Action {
    Async {
      val results = 3
      val query = "paperclip OR \"paper clip\""

      val responsePromise = WS.url("http://search.twitter.com/search.json").withQueryString("q" -> query, "rpp" -> results.toString).get

      responsePromise.map { response =>
        val tweets = Json.parse(response.body).\("results").as[Seq[Tweet]]
        Ok(views.html.twitterrest.tweetlist(tweets))
      }
    }
  }

  def cached() = Action {
    Async {
      val results = 3
      val query = "paperclip OR \"paper clip\""

      Cache.getOrElse("tweets", 10) {
        val responsePromise = WS.url("http://search.twitter.com/search.json")
          .withQueryString("q" -> query, "rpp" -> results.toString).get

        Logger.info("Requesting tweets from Twitter")

        responsePromise.map { response =>
          val tweets = Json.parse(response.body).\("results").as[Seq[Tweet]]
          Ok(views.html.twitterrest.tweetlist(tweets))
        }
      }
    }
  }

  def cached2() = Cached("tweets2", 10) {
    Action {
      Async {
        Logger.info("Requesting tweets from Twitter")

        val results = 3
        val query = "paperclip OR \"paper clip\""

        val responsePromise = WS.url("http://search.twitter.com/search.json").withQueryString("q" -> query, "rpp" -> results.toString).get

        responsePromise map { response =>
          val tweets = Json.parse(response.body).\("results").as[Seq[Tweet]]
          Ok(views.html.twitterrest.tweetlist(tweets))
        }
      }
    }
  }

}