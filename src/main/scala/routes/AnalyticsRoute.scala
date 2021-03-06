package com.pinkstack
package routes

import Domain._

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.{Directives, Route}
import akka.pattern.ask
import akka.util.Timeout
import com.pinkstack.actors.AggregateActor

import java.time.LocalDate
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.reflect.ClassTag

class AnalyticsRoute(aggregateActor: ActorRef)(implicit system: ActorSystem) {

  import Directives._
  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._
  import Serialisation.LocalDateSerde._
  import system.dispatcher

  implicit val timeout: Timeout = 6.second

  val route: Route = pathPrefix("analytics")(concat(get(
    concat(
      path("positive")(complete(fetchAggregate[PositiveCases](FetchPositiveCases))),
      path("positiveByGender")(complete(fetchAggregate[PositiveCasesByGender](FetchPositiveCasesByGender))),
      path("positiveByGenderAndState")(complete(fetchAggregate[PositiveCasesByGenderAndState](FetchPositiveCasesByGenderAndState))),
      path("positiveByDates")(complete(fetchAggregate[PositiveCasesByDates](FetchPositiveCasesByDates))),
      path("positiveByCountryAndDates") {
        parameter(Symbol("country").as[String].*) { countries: Iterable[String] => complete(filterCountries(countries)) }
      },
      path("quarantine") {
        complete(fetchAggregate[QuarantineCasesByCountryAndDuration](FetchQuarantineCasesByCountryAndDuration))
      }
    )
  )))

  private[this] def fetchAggregate[T: ClassTag](aggregate: Domain.FetchAggregate): Future[T] = {
    (aggregateActor ? AggregateActor.GetAggregate(aggregate)).mapTo[T]
  }

  /**
   * Returns all countries with all the dates. The endpoint supports filtering.
   *
   * @param countries
   * @return
   */
  private[this] def filterCountries(countries: Iterable[String]): Future[PositiveCasesByCountryAndDates] = {
    fetchAggregate[PositiveCasesByCountryAndDates](FetchPositiveCasesByCountryAndDates)
      .map { p =>
        Option.when(countries.isEmpty)(p).getOrElse(
          PositiveCasesByCountryAndDates(p.countries.filter { case (country, _) => countries.toSeq.contains(country) }))
      }
  }
}
