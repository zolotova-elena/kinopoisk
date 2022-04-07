package ru.kinopoisk.daemon.services

import scala.concurrent.{ExecutionContext, Future}

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import cats.implicits.catsSyntaxEitherId
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import ru.kinopoisk.daemon.config.AppConfig
import ru.kinopoisk.daemon.errors.{AppError, KinopoiskRequestError, KinopoiskResponseError, UnmarshalError}
import ru.kinopoisk.daemon.models.{KinopoiskError, KinopoiskLog}

class KinopoiskService(
  config: AppConfig,
  log: LoggingAdapter
)(implicit ec: ExecutionContext, system: ActorSystem, mat: Materializer) extends PlayJsonSupport {

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
          Unmarshal(response.entity)
            .to[String]
            .map(content =>
              if (content == "Not Found") {
                Left(KinopoiskRequestError(response))
              } else {
                Right(KinopoiskLog.createKinopoiskLog(page, content))
              }
            )
            .recover { case exception => UnmarshalError(response, exception.getMessage).asLeft }
        case response@HttpResponse(StatusCodes.NotFound, _, _, _) =>
          Unmarshal(response.entity)
            .to[KinopoiskError]
            .map { kinopoiskError => Left(KinopoiskResponseError(kinopoiskError, page)) }
            .recover { case exception => UnmarshalError(response, exception.getMessage).asLeft }
        case response: HttpResponse =>
          log.info(s"something wrong in kinopoisk request; code = ${response.status}")
          Future.successful(Left(KinopoiskRequestError(response)))
      }
  }

}
