package controllers

import akka.actor._

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.iteratee._
import play.api.libs.concurrent._

import akka.util.Timeout
import akka.util.duration._
import akka.pattern.ask

import play.api.Play.current

object Chat extends Controller {

  implicit val timeout = Timeout(1 seconds)
  val room = Akka.system.actorOf(Props[ChatRoom])

  def showRoom(nick: String) = Action { implicit request =>
    Ok(views.html.chat.showRoom(nick))
  }

  def chatSocket(nick: String) = WebSocket.async { request =>
    val channelsFuture = room ? Join(nick)
    channelsFuture.mapTo[(Iteratee[String, _], Enumerator[String])]
      .asPromise
  }
}

case class Join(nick: String)
case class Leave(nick: String)
case class Broadcast(message: String)

class ChatRoom extends Actor {
  var users = Map[String, PushEnumerator[String]]()
  def receive = {
    case Join(nick) => {
      if(!users.contains(nick)) {
        val enumerator = Enumerator.imperative[String]()
        val iteratee = Iteratee.foreach[String]{ message =>
          self ! Broadcast("%s: %s" format (nick, message))
        }.mapDone { _ =>
          self ! Leave(nick)
        }
        users += nick -> enumerator
        broadcast("User %s has joined the room, now %s users"
          format(nick, users.size))
        sender ! (iteratee, enumerator)
      } else {
    	val enumerator =  Enumerator("nick %s is already in use."
    	  format nick)
        val iteratee = Iteratee.ignore
        sender ! (iteratee, enumerator)
      }
    }
    case Leave(nick) => {
      users -= nick
      broadcast("User %s has left the room, %s users left"
        format(nick, users.size))
    }
    case Broadcast(msg: String) => broadcast(msg)
  }

  def broadcast(msg: String) = users.foreach { case (_, enumerator) =>
    enumerator.push(msg)
  }
}