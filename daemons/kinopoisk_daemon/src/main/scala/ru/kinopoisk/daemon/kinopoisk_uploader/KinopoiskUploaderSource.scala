package ru.kinopoisk.daemon.kinopoisk_uploader

import javax.inject.Inject

import scala.concurrent.{ExecutionContext, Future}

import akka.NotUsed
import akka.actor.Cancellable
import akka.event.LoggingAdapter
import akka.stream.scaladsl.Source
import org.joda.time.{DateTime, DateTimeZone}
import ru.kinopoisk.daemon.config.AppConfig
import ru.kinopoisk.daemon.models.KinopoiskLog.INITIAL_PAGE
import ru.kinopoisk.daemon.services.KinopoiskLogService

class KinopoiskUploaderSource @Inject()(
  kinopoiskLogService: KinopoiskLogService,
  log: LoggingAdapter,
  appConfig: AppConfig
)(implicit ec: ExecutionContext) {

  def getLastPage(): Future[Int] = {
    val currentDate = new DateTime(DateTimeZone.UTC).withTimeAtStartOfDay()

    log.info(s"[KINOPOISK_UPLOADER] currentDate = $currentDate")

    kinopoiskLogService.getLastByDate(currentDate).flatMap { kinopoiskLogs =>
      kinopoiskLogs.map(_.page).maxOption.fold {
        kinopoiskLogService.getLast().map {
          case Some(kinopoiskLogLast) => kinopoiskLogLast.page
          case _ => INITIAL_PAGE
        }
      }(Future.successful)
    }
  }

  val value: Source[Int, Cancellable] = {
    Source
      .tick(appConfig.initialDelay, appConfig.interval, NotUsed)
      .mapAsync(1)(_ => getLastPage())
  }
}
