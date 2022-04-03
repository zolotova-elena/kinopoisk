package ru.kinopoisk.daemon.kinopoisk_uploader

import javax.inject.Inject

import scala.concurrent.{ExecutionContext, Future}

import akka.NotUsed
import akka.actor.Cancellable
import akka.stream.scaladsl.Source
import ru.kinopoisk.daemon.config.AppConfig
import ru.kinopoisk.daemon.services.KinopoiskLogService

class KinopoiskUploaderSource @Inject()(
  kinopoiskLogService: KinopoiskLogService,
  appConfig: AppConfig
)(implicit ec: ExecutionContext) {

  def getLast(): Future[Int] = {
    //todo
    kinopoiskLogService.getLast().map { kinopoiskLogLastO =>
      4
    }
  }

  val value: Source[Int, Cancellable] = {
    Source
      .tick(appConfig.initialDelay, appConfig.interval, NotUsed)
      .mapAsync(1)(_ => getLast())
  }
}
