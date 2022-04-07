package ru.kinopoisk.daemon.workers.kinopoisk_uploader

import javax.inject.Inject

import scala.concurrent.Future

import akka.Done
import akka.event.LoggingAdapter
import akka.stream.scaladsl.Sink
import ru.kinopoisk.daemon.models.KinopoiskLog

class KinopoiskUploaderSink @Inject()(
  log: LoggingAdapter
) {

  val value: Sink[KinopoiskLog, Future[Done]] = Sink.foreach { kinopoiskLog =>
    Future.successful(log.info(s"Create kinopoiskLog = ${kinopoiskLog._id.toString()}"))
  }
}
