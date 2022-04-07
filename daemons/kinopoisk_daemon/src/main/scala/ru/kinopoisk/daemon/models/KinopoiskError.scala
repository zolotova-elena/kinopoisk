package ru.kinopoisk.daemon.models

import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.{Reads, __}

case class KinopoiskError(
  code: Int,
  message: String
)

trait KinopoiskErrorJson {
  implicit val reads: Reads[KinopoiskError] = (
    (__ \ "error" \ "code").read[Int] and
    (__ \ "error" \ "message").read[String]
  ) (KinopoiskError.apply _)
}

object KinopoiskError extends KinopoiskErrorJson