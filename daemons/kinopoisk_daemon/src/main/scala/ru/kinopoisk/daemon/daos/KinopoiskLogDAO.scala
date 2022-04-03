package ru.kinopoisk.daemon.daos

import javax.inject.Inject

import scala.concurrent.{ExecutionContext, Future}

import com.softwaremill.tagging.@@
import reactivemongo.api.bson._
import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.api.commands.WriteResult
import ru.kinopoisk.daemon.models.KinopoiskLog
import ru.kinopoisk.daemon.modules.Movies

class KinopoiskLogDAO @Inject()(
  mongoApi: ReactiveMongoApi @@ Movies
)(implicit ec: ExecutionContext) {

  def collection: Future[BSONCollection] = mongoApi.db.map(db => db.collection("kinopoisk_log"))

  def getLast(): Future[Option[KinopoiskLog]] = {
    collection.flatMap(
      _.find(
        BSONDocument(),
        Option.empty[KinopoiskLog]
      ).sort(BSONDocument("create" -> -1)).one[KinopoiskLog]
    )
  }

  def insert(kinopoiskLog: KinopoiskLog): Future[WriteResult] = {
    collection.flatMap(
      _.insert(ordered = false).one(kinopoiskLog)
    )
  }
}
