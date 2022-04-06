package ru.kinopoisk.daemon.models

import enumeratum.values.{StringEnum, StringEnumEntry, StringReactiveMongoBsonValueEnum}

sealed abstract class KinopoiskLogStatus(val value: String) extends StringEnumEntry

object KinopoiskLogStatus extends StringEnum[KinopoiskLogStatus]
  with StringReactiveMongoBsonValueEnum[KinopoiskLogStatus] {

  case object Wait extends KinopoiskLogStatus("wait")
  case object Done extends KinopoiskLogStatus("done")
  case object Error extends KinopoiskLogStatus("error")

  override def values: IndexedSeq[KinopoiskLogStatus] = findValues
}
