package com.pinkstack
package aggregators

import Domain.Person

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Sink}

import java.time.LocalDate
import scala.collection.SortedMap

object AggregateQuarantineCasesByCountryAndDuration {
  type Country = String
  type DaysInQuarantine = String
  type Count = Int
  type TestDate = LocalDate

  def apply(callback: Domain.QuarantineCasesByCountryAndDuration => Unit): Sink[Person, NotUsed] = {
    Flow[Person]
      .filter(_.stillInQuarantine)
      .fold(Map.empty[Country, SortedMap[DaysInQuarantine, Count]]) {
        case (agg, person@Person(_, _, _, country: Country, _, _, _)) =>
          agg ++ Seq(
            country -> (
              agg.getOrElse(country, SortedMap.empty[DaysInQuarantine, Count])
                ++ List(person.quarantineDaysLeftKey -> agg.get(country)
                .flatMap(_.get(person.quarantineDaysLeftKey).map(_ + 1)).getOrElse(1))
              )
          )
      }
      .map(sum => Domain.QuarantineCasesByCountryAndDuration(sum))
      .to(Sink.foreach(wrap => callback(wrap)))
  }
}
