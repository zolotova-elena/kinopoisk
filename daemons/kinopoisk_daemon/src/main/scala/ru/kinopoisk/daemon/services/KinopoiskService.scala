package ru.kinopoisk.daemon.services

import scala.concurrent.{ExecutionContext, Future}

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import ru.kinopoisk.daemon.config.AppConfig
import ru.kinopoisk.daemon.errors.{AppError, KinopoiskRequestError}
import ru.kinopoisk.daemon.models.KinopoiskLog

class KinopoiskService(
  config: AppConfig,
  log: LoggingAdapter
)(implicit ec: ExecutionContext, system: ActorSystem, mat: Materializer) extends {

  def getMoviesByPage(page: Int): Future[Either[AppError, KinopoiskLog]] = {
    val uri = s"${config.kinopoiskUri}/movies/all/page/$page/token/${config.kinopoiskToken}"

    Http()
      .singleRequest(
        HttpRequest(
          method = HttpMethods.GET,
          uri = Uri(uri)
        )
      )
      .flatMap {
        case response@HttpResponse(StatusCodes.OK, _, _, _) =>
          Unmarshal(response.entity).to[String].map(content =>
            Right(KinopoiskLog.createKinopoiskLog(page, content))
          )
        case response: HttpResponse =>
          log.info(s"something wrong in kinopoisk request; code = ${response.status}")
          Future.successful(Left(KinopoiskRequestError(response)))
      }
  }

}
