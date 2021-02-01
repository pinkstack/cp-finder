package com.pinkstack

import scala.io.StdIn

object XApp extends App {

  import akka.actor.ActorSystem
  import akka.stream.scaladsl._

  implicit val system = ActorSystem("dodo")

  import system.dispatcher


  Source(List(1, 2, 3, 4))
    .map(_ * 2)
    .alsoTo(Flow[Int].fold(0) { (agg, c) => agg + c }.to(Sink.foreach { sum => println(s"sum = $sum") }))
    .alsoTo(Flow[Int].fold(0) { (agg, c) => agg + 1 }.to(Sink.foreach { count => println(s"count = $count") }))
    .to(Sink.foreach(println))
    .run()

  StdIn.readLine()
  system.terminate()

}
