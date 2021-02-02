package com.pinkstack

import java.time.LocalDate
import scala.collection.SortedMap

object Domain {

  sealed trait Command

  case class CreatePerson(id: Int,
                          gender: Char,
                          birthDate: LocalDate,
                          isoCountry: String,
                          testDate: LocalDate,
                          testResult: String,
                          intervention: String) extends Command

  case class UpdatePerson(gender: Char,
                          birthDate: LocalDate,
                          isoCountry: String,
                          testDate: LocalDate,
                          testResult: String,
                          intervention: String) extends Command

  case object DeletePerson extends Command

  case object GetPerson extends Command

  trait Event

  case class PersonCreated(id: Int,
                           gender: Char,
                           birthDate: LocalDate,
                           isoCountry: String,
                           testDate: LocalDate,
                           testResult: String,
                           intervention: String) extends Event with CborSerializable

  case class PersonUpdated(gender: Char,
                           birthDate: LocalDate,
                           isoCountry: String,
                           testDate: LocalDate,
                           testResult: String,
                           intervention: String) extends Event with CborSerializable

  case class PersonDeleted() extends Event with CborSerializable

  sealed trait PersonState

  final case object EmptyPerson extends PersonState with CborSerializable

  final case object DeletedPerson extends PersonState with CborSerializable

  final case class Person(id: Int,
                          gender: Char,
                          birthDate: LocalDate,
                          isoCountry: String,
                          testDate: LocalDate,
                          testResult: String,
                          intervention: String) extends PersonState with CborSerializable {
    def positive: Boolean = testResult == "P"

    def male: Boolean = gender == 'M'

    def female: Boolean = !male

    def hadIntervention: Boolean = !intervention.isBlank && intervention != "-"
  }

  object Person {
    val identity: Person = Person(0, 'M', LocalDate.now(), "svn", LocalDate.now(), "N", "-")
  }

  sealed trait AggregateResult

  case class PositiveCases(count: Int) extends AggregateResult

  case class PositiveCasesByGender(male: Int, female: Int) extends AggregateResult

  case class PositiveCasesByGenderAndState(male: Map[String, Int] = Map.empty,
                                           female: Map[String, Int] = Map.empty) extends AggregateResult

  case class PositiveCasesByDates(dates: SortedMap[LocalDate, Int] = SortedMap.empty) extends AggregateResult

  case class PositiveCasesByCountryAndDates(countries: Map[String, SortedMap[LocalDate, Int]] = Map.empty) extends AggregateResult

  sealed trait FetchAggregate

  case object FetchPositiveCases extends FetchAggregate

  case object FetchPositiveCasesByGender extends FetchAggregate

  case object FetchPositiveCasesByGenderAndState extends FetchAggregate

  case object FetchPositiveCasesByDates extends FetchAggregate

  case object FetchPositiveCasesByCountryAndDates extends FetchAggregate

}
