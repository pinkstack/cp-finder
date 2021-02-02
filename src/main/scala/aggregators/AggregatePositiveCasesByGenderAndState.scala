package com.pinkstack
package aggregators

import Domain.PositiveCasesByGenderAndState

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Sink}

object AggregatePositiveCasesByGenderAndState {

  import Domain.Person

  type Male = String
  type Female = String
  type Count = Int
  type Agg = (Map[Male, Count], Map[Male, Count])

  def apply(callback: Domain.PositiveCasesByGenderAndState => Unit): Sink[Person, NotUsed] =
    Flow[Domain.Person]
      .filter(person => person.positive && person.hadIntervention)
      .fold[Agg]((Map.empty[Male, Count], Map.empty[Female, Count])) {
        case ((maleAgg, femaleAgg), person@Person(_, _, _, _, _, _, intervention)) if person.male =>
          (maleAgg + (intervention -> maleAgg.get(intervention).map(_ + 1).getOrElse(1)), femaleAgg)

        case ((maleAgg, femaleAgg), Person(_, _, _, _, _, _, intervention)) =>
          (maleAgg, femaleAgg + (intervention -> femaleAgg.get(intervention).map(_ + 1).getOrElse(1)))
      }
      .map { case (male, female) => PositiveCasesByGenderAndState(male, female) }
      .to(Sink.foreach(callback(_)))
}
