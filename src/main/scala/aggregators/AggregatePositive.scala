package com.pinkstack
package aggregators

import Domain.{Person, PositiveCases}

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Sink}

import java.time.LocalDate
import scala.collection.SortedMap

object AggregatePositive {
  def apply(callback: PositiveCases => Unit): Sink[Person, NotUsed] =
    Flow[Person]
      .filter(_.positive)
      .fold(0)((agg, _) => agg + 1)
      .map(sum => Domain.PositiveCases(sum))
      .to(Sink.foreach(wrap => callback(wrap)))
}

