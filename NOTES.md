# Notes

## Event

- https://tl-hack.incubatehub.com/amas/opening-ceremony?ref=xqPyWcWN


## "Q"

$ q -d ";" -H "SELECT Gender, count(*) FROM ./data/covidPeople.csv GROUP BY Gender"

$ q -d ";" -H "SELECT Intervention, count(*) FROM ./data/covidPeople.csv GROUP BY Intervention"

## CQRS

- https://github.com/ingenuiq/es-cqrs-akka-full-example

## Serialization

- https://doc.akka.io/docs/akka/current/serialization-jackson.html
- https://tinkertailor.dev/2020/11/07/persistence-through-event-sourcing/
- https://blog.codecentric.de/en/2016/08/cqrs-es-akka/
- https://medium.com/@yuriigorbylov/akka-ask-antipattern-8361e9698b20
- https://doc.akka.io/docs/akka/current/persistence.html
- https://alvinalexander.com/java/jwarehouse/akka-2.3/akka-persistence/src/main/resources/reference.conf.shtml
- https://github.com/ScalaConsultants/akka-persistence-eventsourcing

- https://github.com/gunnigylfa/counter-actor/blob/3.1-persistence/src/main/scala/com/counter/CounterRoutes.scala
