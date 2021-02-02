package com.pinkstack
package actors

import aggregators.{AggregatePositive, AggregatePositiveByGender, AggregatePositiveCasesByCountryAndDates, AggregatePositiveCasesByDates, AggregatePositiveCasesByGenderAndState}

import akka.NotUsed
import akka.actor._
import akka.persistence.query.journal.leveldb.scaladsl.LeveldbReadJournal
import akka.persistence.query.{EventEnvelope, PersistenceQuery}
import akka.stream.scaladsl._

class AggregateActor extends Actor with ActorLogging {

  import Domain._

  import AggregateActor._
  import context.dispatcher

  import scala.concurrent.duration._

  implicit val string: ActorSystem = context.system

  var aggregatedPositiveCases: PositiveCases = PositiveCases(0)
  var aggregatedPositiveCasesByGender: PositiveCasesByGender = PositiveCasesByGender(0, 0)
  var aggregatedPositiveCasesByGenderAndState: PositiveCasesByGenderAndState = PositiveCasesByGenderAndState()
  var aggregatedPositiveCasesByDates: PositiveCasesByDates = PositiveCasesByDates()
  var aggregatedPositiveCasesByCountryAndDates: PositiveCasesByCountryAndDates = PositiveCasesByCountryAndDates()

  context.system.scheduler.scheduleWithFixedDelay(0.seconds, 5.seconds, self, Tick)

  def receive: Receive = {
    case Tick =>
      aggregate

    case Aggregated(result: PositiveCases) =>
      aggregatedPositiveCases = result

    case Aggregated(result: PositiveCasesByGender) =>
      aggregatedPositiveCasesByGender = result

    case Aggregated(result: PositiveCasesByGenderAndState) =>
      aggregatedPositiveCasesByGenderAndState = result

    case Aggregated(result: PositiveCasesByDates) =>
      aggregatedPositiveCasesByDates = result

    case Aggregated(result: PositiveCasesByCountryAndDates) =>
      aggregatedPositiveCasesByCountryAndDates = result

    case GetAggregate(FetchPositiveCases) =>
      sender() ! aggregatedPositiveCases

    case GetAggregate(FetchPositiveCasesByGender) =>
      sender() ! aggregatedPositiveCasesByGender

    case GetAggregate(FetchPositiveCasesByGenderAndState) =>
      sender() ! aggregatedPositiveCasesByGenderAndState

    case GetAggregate(FetchPositiveCasesByDates) =>
      sender() ! aggregatedPositiveCasesByDates

    case GetAggregate(FetchPositiveCasesByCountryAndDates) =>
      sender() ! aggregatedPositiveCasesByCountryAndDates
  }

  private[this] val queries: LeveldbReadJournal = PersistenceQuery(context.system)
    .readJournalFor[LeveldbReadJournal](LeveldbReadJournal.Identifier)

  private[this] val enrich: Flow[String, Person, NotUsed] = {
    Flow[String]
      .flatMapConcat { persistenceId =>
        queries.currentEventsByPersistenceId(persistenceId).map {
          envelope: EventEnvelope => envelope.event.asInstanceOf[Domain.Event]
        }.fold(Person.identity) {
          case (person: PersonState, event: Domain.Event) =>
            personFromEvent(person, event) match {
              case DeletedPerson => Person.identity
              case person: PersonState => person.asInstanceOf[Person]
            }
        }
      }.filter(_.id != 0)
  }

  private[this] def currentPeople(): Source[Person, NotUsed] =
    queries.currentPersistenceIds()
      .filter(_.contains("person-"))
      .via(enrich)

  private[this] def aggregate = {
    currentPeople()
      .async
      .alsoTo(AggregatePositive.apply(self ! Aggregated(_)))
      .alsoTo(AggregatePositiveByGender.apply(self ! Aggregated(_)))
      .alsoTo(AggregatePositiveCasesByGenderAndState.apply(self ! Aggregated(_)))
      .alsoTo(AggregatePositiveCasesByDates.apply(self ! Aggregated(_)))
      .alsoTo(AggregatePositiveCasesByCountryAndDates.apply(self ! Aggregated(_)))
      .runWith(Sink.ignore)
  }

  private[this] def personFromEvent(person: PersonState, event: Domain.Event): PersonState = event match {
    case PersonCreated(id, gender, birthDate, isoCountry, testDate, testResult, intervention) =>
      Person(id, gender, birthDate, isoCountry, testDate, testResult, intervention)
    case PersonUpdated(gender, birthDate, isoCountry, testDate, testResult, intervention) if person.isInstanceOf[Person] =>
      person.asInstanceOf[Person].copy(gender = gender, birthDate = birthDate,
        isoCountry = isoCountry, testDate = testDate, testResult = testResult, intervention = intervention)
    case _: PersonDeleted =>
      DeletedPerson
  }
}


object AggregateActor {

  import Domain._

  final case object Tick

  final case class Aggregated(result: AggregateResult)

  final case class GetAggregate(fetchAggregate: FetchAggregate)

}
