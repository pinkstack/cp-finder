package com.pinkstack
package aggregators

import Domain.Person

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Sink}

import java.time.LocalDate
import scala.collection.SortedMap

object AggregatePositiveCasesByDates {
  def apply(callback: Domain.PositiveCasesByDates => Unit): Sink[Person, NotUsed] = {
    Flow[Person]
      .filter(_.positive)
      .fold(SortedMap.empty[LocalDate, Int]) { case (agg, person: Person) =>
        agg ++ List(person.testDate -> agg.get(person.testDate).map(_ + 1).getOrElse(1))
      }
      .map(sum => Domain.PositiveCasesByDates(sum))
      .to(Sink.foreach(wrap => callback(wrap)))
  }
}

