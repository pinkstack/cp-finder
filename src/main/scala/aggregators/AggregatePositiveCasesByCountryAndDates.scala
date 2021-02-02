package com.pinkstack
package aggregators

import Domain.Person

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Sink}

import java.time.LocalDate
import scala.collection.SortedMap

object AggregatePositiveCasesByCountryAndDates {
  type Country = String
  type TestDate = LocalDate
  type Count = Int

  def apply(callback: Domain.PositiveCasesByCountryAndDates => Unit): Sink[Person, NotUsed] = {
    Flow[Person]
      .filter(_.positive)
      .fold(Map.empty[Country, SortedMap[TestDate, Count]]) {
        case (agg, Person(_, _, _, country: Country, testDate: TestDate, _, _)) =>
          agg ++ Seq(
            country -> (
              agg.getOrElse(country, SortedMap.empty[TestDate, Count])
                ++ List(testDate -> agg.get(country).flatMap(_.get(testDate).map(_ + 1)).getOrElse(1))
              ))
      }
      .map(sum => Domain.PositiveCasesByCountryAndDates(sum))
      .to(Sink.foreach(wrap => callback(wrap)))
  }
}

