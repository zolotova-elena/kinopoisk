package ru.kinopoisk.daemon.kinopoisk_uploader

import akka.NotUsed
import akka.actor.Cancellable
import akka.stream.scaladsl.Source
import ru.kinopoisk.daemon.config.AppConfig

import javax.inject.Inject
import scala.concurrent.Future

class KinopoiskUploaderSource @Inject()(
  appConfig: AppConfig
) {

  def getLast(): Future[Int] = {
    //todo
    Future.successful(4)
  }

  val value: Source[Int, Cancellable] = {
    Source
      .tick(appConfig.initialDelay, appConfig.interval, NotUsed)
      .mapAsync(1)(_ => getLast())
  }
}
