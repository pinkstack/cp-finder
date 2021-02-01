package com.pinkstack
package actors

import akka.actor.ActorLogging
import akka.persistence.{PersistentActor, SnapshotMetadata, SnapshotOffer}

class PersonActor(id: Int) extends PersistentActor with ActorLogging {

  import Domain._

  val persistenceId: String = s"person-$id"

  var state: PersonState = EmptyPerson

  def updateState(event: Event): Unit = {
    event match {
      case PersonCreated(id, gender, birthDate, isoCountry, testDate, testResult, intervention) =>
        state = Person(id, gender, birthDate, isoCountry, testDate, testResult, intervention)
        context.become(initializedPerson)
      case PersonUpdated(gender, birthDate, isoCountry, testDate, testResult, intervention)
        if state.isInstanceOf[Person] =>
        state = Person(
          id = state.asInstanceOf[Person].id,
          gender, birthDate, isoCountry, testDate, testResult, intervention)
      case _: PersonDeleted =>
        state = DeletedPerson
        context.become(deletedPerson)
    }
  }

  override def receiveRecover: Receive = {
    case event: Event => updateState(event)
    case SnapshotOffer(_: SnapshotMetadata, snapshot: PersonState) =>
      state = snapshot
  }

  def emptyPerson: Receive = {
    case c: CreatePerson =>
      persist(PersonCreated(c.id, c.gender, c.birthDate, c.isoCountry,
        c.testDate, c.testResult, c.intervention)) { event =>
        updateState(event)
        context.system.eventStream.publish(event)
        sender() ! s"200 - Person successfully created with id $id."
      }

    case GetPerson =>
      sender() ! state.toString

    case c: Any =>
      sender() ! s"500 - [EmptyPerson] - Got ${c.getClass.getName}"
  }

  def initializedPerson: Receive = {
    case c: UpdatePerson =>
      persist(PersonUpdated(c.gender, c.birthDate, c.isoCountry,
        c.testDate, c.testResult, c.intervention)) { event =>
        updateState(event)
        context.system.eventStream.publish(event)
        sender() ! s"200 - Person updated successfully."
      }

    case GetPerson =>
      sender() ! state.toString

    case DeletePerson =>
      persist(PersonDeleted()) { event =>
        updateState(event)
        context.system.eventStream.publish(event)
        sender() ! s"200 - Person successfully deleted."
      }

    case c: Any =>
      sender() ! s"500 - [InitializedPerson] - Unsupported command ${c.getClass.getName}"

  }

  def deletedPerson: Receive = {
    case c: Any =>
      sender() ! s"404 - [DeletedPerson] - Unsupported command ${c.getClass.getName}"
  }

  override def receiveCommand: Receive = emptyPerson

}
