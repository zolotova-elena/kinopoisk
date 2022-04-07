package ru.kinopoisk.daemon.workers.kinopoisk_uploader

import javax.inject.Inject

import scala.concurrent.{ExecutionContext, Future}

import akka.NotUsed
import akka.actor.Cancellable
import akka.event.LoggingAdapter
import akka.stream.scaladsl.Source
import cats.data.EitherT
import cats.implicits._
import org.joda.time.{DateTime, DateTimeZone}
import ru.kinopoisk.daemon.config.AppConfig
import ru.kinopoisk.daemon.models.KinopoiskLog.INITIAL_PAGE
import ru.kinopoisk.daemon.services.KinopoiskLogService

class KinopoiskUploaderSource @Inject()(
  kinopoiskLogService: KinopoiskLogService,
  log: LoggingAdapter,
  appConfig: AppConfig
)(implicit ec: ExecutionContext) {

  private def getPageNumber(pageO: Option[Int]): Future[Int] = {
    pageO.fold {
      kinopoiskLogService.getLast().map {
        case Some(kinopoiskLogLast) => kinopoiskLogLast.page
        case _ => INITIAL_PAGE
      }
    }(Future.successful)
  }

  def logWithTag(msg: String): Unit = {
    log.info(s"[KINOPOISK_UPLOADER_SOURCE] $msg")
  }

  def getLastPage(): Future[Option[Int]] = {
    val currentDate = new DateTime(DateTimeZone.UTC).withTimeAtStartOfDay()
    log.info(s"[KINOPOISK_UPLOADER] currentDate = $currentDate")

    (for {
      kinopoiskLogs <- EitherT(kinopoiskLogService.getLastByDate(currentDate).map(Right(_)).recover { case error =>
        Left(logWithTag(s"can't get kinopoiskLogs by date = $currentDate, error: ${error.toString}"))
      })

      _ <- EitherT.cond[Future](kinopoiskLogs.size < appConfig.maxRequestsInDay, (),
        logWithTag(s"the maximum number of requests for today has been completed, ${kinopoiskLogs.size} / ${appConfig.maxRequestsInDay})")
      )

      lastPage <- EitherT(getPageNumber(kinopoiskLogs.map(_.page).maxOption).map(Right(_)).recover { case error =>
        Left(logWithTag(s"can't get last page by date = $currentDate, error: ${error.toString}"))
      })
    } yield {
      lastPage
    }).value.map(_.toOption)
  }

  val value: Source[Int, Cancellable] = {
    Source
      .tick(appConfig.kinopoiskUploaderWorkerInitialDelay, appConfig.kinopoiskUploaderWorkerInterval, NotUsed)
      .mapAsync(1)(_ => getLastPage())
      .collect { case Some(lastPage) => lastPage }
  }
}
