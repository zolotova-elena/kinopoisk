package ru.kinopoisk.daemon.kinopoisk_uploader

import akka.Done
import akka.event.LoggingAdapter
import akka.stream.scaladsl.Sink

import javax.inject.Inject
import scala.concurrent.Future

class KinopoiskUploaderSink @Inject()(
  log: LoggingAdapter
) {

  val value: Sink[Int, Future[Done]] = Sink.foreach { _ =>
    Future.successful(log.info("in Sink"))
  }
}
