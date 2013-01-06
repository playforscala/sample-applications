package controllers

import scala.concurrent.duration.DurationInt

import akka.actor.{ Actor, Props, actorRef2Scala }
import akka.pattern.ask
import akka.util.Timeout
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.libs.iteratee.{ Concurrent, Enumerator, Iteratee }
import play.api.mvc.{ Action, Controller, WebSocket }
import scala.language.postfixOps

object Chat extends Controller {

  implicit val timeout = Timeout(1 seconds)
  val room = Akka.system.actorOf(Props[ChatRoom])

  def showRoom(nick: String) = Action { implicit request =>
    Ok(views.html.chat.showRoom(nick))
  }

  def chatSocket(nick: String) = WebSocket.async { request =>
    val channelsFuture = room ? Join(nick)
    channelsFuture.mapTo[(Iteratee[String, _], Enumerator[String])]
  }
}

case class Join(nick: String)
case class Leave(nick: String)
case class Broadcast(message: String)

class ChatRoom extends Actor {
  var users = Set[String]()
  val (enumerator, channel) = Concurrent.broadcast[String]
  def receive = {
    case Join(nick) => {
      if (!users.contains(nick)) {
        val iteratee = Iteratee.foreach[String] { message =>
          self ! Broadcast("%s: %s" format (nick, message))
        }.mapDone { _ =>
          self ! Leave(nick)
        }
        users += nick
        channel.push("User %s has joined the room, now %s users"
          format (nick, users.size))
        sender ! (iteratee, enumerator)
      } else {
        val enumerator = Enumerator("nick %s is already in use."
          format nick)
        val iteratee = Iteratee.ignore
        sender ! (iteratee, enumerator)
      }
    }
    case Leave(nick) => {
      users -= nick
      channel.push("User %s has left the room, %s users left"
        format (nick, users.size))
    }
    case Broadcast(msg: String) => channel.push(msg)
  }

}