package com.pinkstack
package routes

import Domain.{DeletePerson, GetPerson}
import actors.PeopleActor

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.{Directives, Route}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._

class PeopleRoute(people: ActorRef)
                 (implicit system: ActorSystem) {

  import Serialisation.LocalDateSerde._

  import Directives._
  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._

  implicit val timeout: Timeout = 1.second

  val route: Route =
  // logRequestResult("people-route") {
    pathPrefix("people")(concat(
      post(createPerson),
      path(IntNumber) { implicit id =>
        get(getPerson) ~ patch(updatePerson) ~ delete(deletePerson)
      }
    ))
  //}

  def createPerson: Route =
    entity(as[Domain.CreatePerson]) { command =>
      complete((people ? PeopleActor.CommandWrapper(command.id, command)).mapTo[String])
    }

  def updatePerson()(implicit id: Int): Route =
    entity(as[Domain.UpdatePerson]) { command =>
      complete((people ? PeopleActor.CommandWrapper(id, command)).mapTo[String])
    }

  def deletePerson()(implicit id: Int): Route =
    complete((people ? PeopleActor.CommandWrapper(id, DeletePerson)).mapTo[String])

  def getPerson()(implicit id: Int): Route =
    complete((people ? PeopleActor.CommandWrapper(id, GetPerson)).mapTo[String])
}
