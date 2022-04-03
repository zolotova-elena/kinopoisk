package ru.kinopoisk.daemon.kinopoisk_uploader

import javax.inject.Inject

import scala.concurrent.Future

import akka.Done
import akka.event.LoggingAdapter
import akka.stream.scaladsl.Sink

class KinopoiskUploaderSink @Inject()(
  log: LoggingAdapter
) {

  val value: Sink[Int, Future[Done]] = Sink.foreach { _ =>
    Future.successful(log.info("in Sink"))
  }
}
