package ru.kinopoisk.daemon.workers.kinopoisk_parser

import javax.inject.Inject

import scala.concurrent.Future

import akka.NotUsed
import akka.actor.Cancellable
import akka.event.LoggingAdapter
import akka.stream.scaladsl.Source
import ru.kinopoisk.daemon.config.AppConfig
import ru.kinopoisk.daemon.models.KinopoiskLog
import ru.kinopoisk.daemon.services.KinopoiskLogService

class KinopoiskParserSource @Inject()(
  kinopoiskLogService: KinopoiskLogService,
  log: LoggingAdapter,
  appConfig: AppConfig
) {

  def getKinopoiskLog(): Future[Option[KinopoiskLog]] = {
    kinopoiskLogService.getOlderWait()
  }

  val value: Source[KinopoiskLog, Cancellable] = {
    Source
      .tick(appConfig.kinopoiskParserWorkerInitialDelay, appConfig.kinopoiskParserWorkerInterval, NotUsed)
      .mapAsync(1)(_ => getKinopoiskLog())
      .collect { case Some(kinopoiskLog) => kinopoiskLog }
  }
}
