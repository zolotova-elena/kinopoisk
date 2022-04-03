package ru.kinopoisk.daemon.services

import scala.concurrent.{ExecutionContext, Future}

import org.joda.time.{DateTime, DateTimeZone}
import ru.kinopoisk.daemon.daos.KinopoiskLogDAO
import ru.kinopoisk.daemon.models.KinopoiskLog

class KinopoiskLogService(
  kinopoiskLogDAO: KinopoiskLogDAO
)(implicit ec: ExecutionContext) {

  def getLast(): Future[Option[KinopoiskLog]] = {
    for {
      //insertRes <- insertTestItem()
      last <- kinopoiskLogDAO.getLast()
    } yield {
      last
    }
  }

  def insertTestItem() = {
    kinopoiskLogDAO.insert(
      KinopoiskLog(
        page = 1,
        content = "",
        created = (new DateTime(DateTimeZone.UTC).getMillis / 1000).toInt,
        updated = (new DateTime(DateTimeZone.UTC).getMillis / 1000).toInt
      )
    )
  }

}
