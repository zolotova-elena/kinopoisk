package ru.kinopoisk.daemon.services

import scala.concurrent.{ExecutionContext, Future}

import org.joda.time.DateTime
import reactivemongo.api.commands.WriteResult
import ru.kinopoisk.daemon.daos.KinopoiskLogDAO
import ru.kinopoisk.daemon.models.KinopoiskLog

class KinopoiskLogService(
  kinopoiskLogDAO: KinopoiskLogDAO
)(implicit ec: ExecutionContext) {

  def getLast(): Future[Option[KinopoiskLog]] = kinopoiskLogDAO.getLast()

  def getOlderWait(): Future[Option[KinopoiskLog]] = kinopoiskLogDAO.getOlderWait()

  def getLastByDate(currentDateTime: DateTime): Future[List[KinopoiskLog]] = {
    kinopoiskLogDAO.getLastByDate(currentDateTime)
  }

  def insert(kinopoiskLog: KinopoiskLog): Future[WriteResult] = {
    kinopoiskLogDAO.insert(kinopoiskLog)
  }
}
