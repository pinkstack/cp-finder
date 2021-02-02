package com.pinkstack

import actors.{AggregateActor, PeopleActor}
import routes.{AnalyticsRoute, PeopleRoute}
import sweetjava.Banner

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route

import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.StdIn
import scala.util.{Failure, Success}

object Main extends {
  def startServer(routes: Route)(implicit system: ActorSystem): Unit = {
    val f = Http().newServerAt("0.0.0.0", port = 8080).bind(routes)

    f.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info(Banner.showBanner(address.getHostString, address.getPort))
        system.log.info("Server at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(exception) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", exception)
        system.terminate()
    }
  }

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("cp-finder")
    val people = system.actorOf(Props(classOf[PeopleActor]), name = "people")
    val aggregator = system.actorOf(Props(classOf[AggregateActor]), name = "aggregator")
    val peopleRoute = new PeopleRoute(people).route
    val analyticsRoute = new AnalyticsRoute(aggregator).route

    val routes: Route = {
      import akka.http.scaladsl.server.Directives._
      peopleRoute ~ analyticsRoute
    }

    startServer(routes)
    StdIn.readLine()
    system.terminate()
  }
}


