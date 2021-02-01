package com.pinkstack

import cats.implicits._
import io.circe.{Decoder, Encoder, KeyEncoder}

import java.time.LocalDate
import java.time.format.DateTimeFormatter

trait CborSerializable {
  // https://doc.akka.io/docs/akka/current/serialization-jackson.html
}

object Serialisation {

  object LocalDateSerde {

    private[this] val pattern: DateTimeFormatter = DateTimeFormatter.ofPattern("d.MM.yyyy")

    implicit val decoder: Decoder[LocalDate] = Decoder.decodeString
      .emap(LocalDate.parse(_, pattern).asRight)

    implicit val encoder: Encoder[LocalDate] = Encoder.encodeString.contramap[LocalDate](_.format(pattern))

    implicit val localDateKeyEncoder: KeyEncoder[LocalDate] = (key: LocalDate) => key.format(pattern)
  }

}
