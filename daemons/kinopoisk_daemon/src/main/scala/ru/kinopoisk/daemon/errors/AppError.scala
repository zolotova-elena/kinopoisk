package ru.kinopoisk.daemon.errors

import akka.http.scaladsl.model.HttpResponse
import ru.kinopoisk.daemon.models.KinopoiskError

sealed class AppError

final case class KinopoiskRequestError(response: HttpResponse) extends AppError

final case class KinopoiskResponseError(kinopoiskError: KinopoiskError, page: Int) extends AppError

final case class UnmarshalError(response: HttpResponse, exception: String) extends AppError
