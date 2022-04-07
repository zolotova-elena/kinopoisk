package ru.kinopoisk.daemon.workers.kinopoisk_uploader

import javax.inject.Inject

import scala.concurrent.ExecutionContext

import akka.NotUsed
import akka.event.LoggingAdapter
import akka.stream.scaladsl.{Flow, Source}
import ru.kinopoisk.daemon.errors.AppError
import ru.kinopoisk.daemon.models.KinopoiskLog
import ru.kinopoisk.daemon.services.{KinopoiskLogService, KinopoiskService}

class KinopoiskUploaderFlow @Inject()(
  kinopoiskLogService: KinopoiskLogService,
  kinopoiskService: KinopoiskService,
  log: LoggingAdapter
)(implicit ec: ExecutionContext) {

  type ErrorOr[T] = Either[AppError, T]

  def errorFlow[T](f: AppError => Unit): Flow[ErrorOr[T], T, NotUsed] = {
    Flow[ErrorOr[T]]
      .map(_.fold({ fa => f(fa); None }, Some[T]))
      .collect { case Some(value) => value }
  }

  def getMovies(): Flow[Int, KinopoiskLog, NotUsed] = {
    Flow[Int].flatMapConcat { lastPage =>
      Source.future(kinopoiskService.getMoviesByPage(lastPage + 1))
        .via(errorFlow(error => log.info("[ERROR] = {}", error)))
    }
  }

  def saveContent(): Flow[KinopoiskLog, KinopoiskLog, NotUsed] = {
    Flow[KinopoiskLog].mapAsync(1) { kinopoiskLog =>
      kinopoiskLogService.insert(kinopoiskLog).map(_ => kinopoiskLog)
    }
  }

  val value: Flow[Int, KinopoiskLog, NotUsed] = {
    Flow[Int]
      .log("After source, last page")
      .via(getMovies())
      .via(saveContent())
  }
}
