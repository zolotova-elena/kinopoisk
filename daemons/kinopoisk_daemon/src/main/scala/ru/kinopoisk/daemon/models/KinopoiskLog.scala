package ru.kinopoisk.daemon.models

import reactivemongo.api.bson.{BSONDocumentHandler, BSONObjectID, Macros}

case class KinopoiskLog(
  _id: BSONObjectID = BSONObjectID.generate(),
  page: Int,
  content: String,
  created: Int,
  updated: Int
)

trait KinopoiskLogBson {
  implicit val handler: BSONDocumentHandler[KinopoiskLog] = Macros.handler[KinopoiskLog]
}

object KinopoiskLog extends KinopoiskLogBson
