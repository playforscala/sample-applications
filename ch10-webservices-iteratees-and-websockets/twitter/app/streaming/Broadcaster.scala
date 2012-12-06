package streaming

import akka.actor.{ Actor, actorRef2Scala }
import play.api.Logger
import play.api.libs.iteratee.{ Enumerator, Iteratee, PushEnumerator }
import play.api.libs.json.JsValue

case object Subscribe
case class Unsubscribe(enumerator: Enumerator[_])
case class Broadcast(message: JsValue)

class Broadcaster extends Actor {

  // Prefer a var with immutable object over a val with mutable object.
  // We can safely send a reference to this list out of our actor, for example
  // in a response to a 'report' message, since the list itself is immutable.
  var subscribers = List[PushEnumerator[JsValue]]()

  def receive = {
    case Subscribe => {
      val enumerator = Enumerator.imperative[JsValue]()
      val iteratee = Iteratee.foreach[JsValue](_ => ()).mapDone(_ =>
        self ! Unsubscribe(enumerator))
      subscribers = subscribers :+ enumerator
      Logger.info("Someone subscribed. We now have %s subscribers." format subscribers.length)
      sender ! (iteratee, enumerator)
    }
    case Unsubscribe(subscriber) => {
      subscribers = subscribers.filterNot(_ == subscriber)
      Logger.info("Someone unsubscribed. We now have %s subscribers." format subscribers.length)
    }
    case Broadcast(message) => {
      subscribers.foreach(_.push(message))
    }
  }

}
