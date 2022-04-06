package ru.kinopoisk.daemon.models

import org.joda.time.{DateTime, DateTimeZone}
import reactivemongo.api.bson.Macros.Annotations.Key
import reactivemongo.api.bson.{BSONDocumentHandler, BSONObjectID, Macros}

case class KinopoiskLog(
  _id: BSONObjectID = BSONObjectID.generate(),
  page: Int,
  status: KinopoiskLogStatus,
  contentType: KinopoiskLogContentType,
  content: String,
  created: Int,
  @Key("updated") updatedO: Option[Int] = None
)

trait KinopoiskLogBson {
  implicit val handler: BSONDocumentHandler[KinopoiskLog] = Macros.handler[KinopoiskLog]
}

object KinopoiskLog extends KinopoiskLogBson {

  val INITIAL_PAGE = 0

  def createKinopoiskLog(page: Int, content: String): KinopoiskLog = {
    KinopoiskLog(
      page = page,
      status = KinopoiskLogStatus.Wait,
      contentType = KinopoiskLogContentType.Movies,
      created = (new DateTime(DateTimeZone.UTC).getMillis / 1000).toInt,
      content = content
    )
  }
}
