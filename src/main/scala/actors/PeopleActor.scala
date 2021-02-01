package com.pinkstack
package actors

import akka.actor.{Actor, ActorLogging, ActorNotFound, ActorRef, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout

import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.duration._

class PeopleActor extends Actor with ActorLogging {
  type Response = String

  import PeopleActor._

  implicit val timeout: Timeout = 2.second
  val maxChildren = 100
  var children: mutable.Queue[ActorRef] = mutable.Queue.empty[ActorRef]

  import context.dispatcher

  override def receive: Receive = {
    case CommandWrapper(id: Int, command: Domain.Command) =>
      findOrCreate(id)(stopOldest)
        .flatMap(ref => ref.ask(command))
        .pipeTo(sender())
  }

  def findOrCreate(id: Int)(afterCreation: ActorRef => Unit): Future[ActorRef] =
    context.actorSelection(s"person-$id")
      .resolveOne(1.second)
      .recover {
        case _: ActorNotFound =>
          val ref = context.actorOf(Props(new PersonActor(id)), s"person-$id")
          afterCreation(ref)
          ref

      }

  def stopOldest(newActor: ActorRef): Unit = {
    children += newActor
    if (children.size >= maxChildren) context.stop(children.dequeue())
  }
}

object PeopleActor {

  final case class CommandWrapper(id: Int, command: Domain.Command)

}
