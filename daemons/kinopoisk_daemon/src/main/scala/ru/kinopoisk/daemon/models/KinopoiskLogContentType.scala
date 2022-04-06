package ru.kinopoisk.daemon.models

import enumeratum.values.{StringEnum, StringEnumEntry, StringReactiveMongoBsonValueEnum}

sealed abstract class KinopoiskLogContentType(val value: String) extends StringEnumEntry

object KinopoiskLogContentType extends StringEnum[KinopoiskLogContentType]
  with StringReactiveMongoBsonValueEnum[KinopoiskLogContentType] {

  case object Movies extends KinopoiskLogContentType("movies")
  case object TvSeries extends KinopoiskLogContentType("tv-series")

  override def values: IndexedSeq[KinopoiskLogContentType] = findValues
}
