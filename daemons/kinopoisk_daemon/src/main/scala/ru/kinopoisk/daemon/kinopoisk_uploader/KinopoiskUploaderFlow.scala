package ru.kinopoisk.daemon.kinopoisk_uploader

import javax.inject.Inject

import akka.NotUsed
import akka.stream.scaladsl.Flow

class KinopoiskUploaderFlow @Inject()(

){

  def changedValue(): Flow[Int, Int, NotUsed] = {
    Flow[Int].map { value =>
      value * 3
    }
  }

  val value: Flow[Int, Int, NotUsed] = {
    Flow[Int]
      .log("After source")
      .via(changedValue())
  }
}
