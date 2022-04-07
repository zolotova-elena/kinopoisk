package ru.kinopoisk.daemon.workers.kinopoisk_parser

import javax.inject.Inject

import scala.concurrent.{ExecutionContext, Future}

import akka.NotUsed
import akka.stream.scaladsl.Flow
import cats.implicits.catsSyntaxApplicativeId
import ru.kinopoisk.daemon.models.KinopoiskLog

class KinopoiskParserFlow @Inject()(

)(implicit ec: ExecutionContext) {

  def parser(): Flow[KinopoiskLog, KinopoiskLog, NotUsed] = {
    Flow[KinopoiskLog].mapAsync(1) { kinopoiskLog =>
      //todo parsed by type

      kinopoiskLog.pure[Future]
    }
  }

  val value: Flow[KinopoiskLog, KinopoiskLog, NotUsed] = {
    Flow[KinopoiskLog]
      .log("After source, older kinopoiskLog")
      .via(parser())
  }
}
