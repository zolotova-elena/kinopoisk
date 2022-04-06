package ru.kinopoisk.daemon.errors

import akka.http.scaladsl.model.HttpResponse

sealed class AppError

final case class KinopoiskRequestError(response: HttpResponse) extends AppError
