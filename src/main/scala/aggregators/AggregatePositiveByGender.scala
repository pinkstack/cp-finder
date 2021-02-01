package com.pinkstack
package aggregators

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Sink}

object AggregatePositiveByGender {
  type Male = Int
  type Female = Int
  type Agg = (Male, Female)

  def apply(callback: Domain.PositiveCasesByGender => Unit): Sink[Domain.Person, NotUsed] = {
    Flow[Domain.Person]
      .filter(_.positive)
      .fold[Agg]((0, 0)) { case ((aggMale, aggFemale), person: Domain.Person) =>
        (
          Option.when(person.male)(aggMale + 1).getOrElse(aggMale),
          Option.when(person.female)(aggFemale + 1).getOrElse(aggFemale),
        )
      }
      .map { case (male, female) => Domain.PositiveCasesByGender(male, female) }
      .to(Sink.foreach(wrap => callback(wrap)))
  }
}
