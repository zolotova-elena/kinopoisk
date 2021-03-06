package ru.kinopoisk.daemon.daos

import javax.inject.Inject

import scala.concurrent.{ExecutionContext, Future}

import com.softwaremill.tagging.@@
import org.joda.time.DateTime
import reactivemongo.api.bson._
import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.{Cursor, ReadPreference}
import ru.kinopoisk.daemon.models.{KinopoiskLog, KinopoiskLogStatus}
import ru.kinopoisk.daemon.modules.Movies

class KinopoiskLogDAO @Inject()(
  mongoApi: ReactiveMongoApi @@ Movies
)(implicit ec: ExecutionContext) {

  def collection: Future[BSONCollection] = mongoApi.db.map(db => db.collection("kinopoisk_log"))

  def getLastByDate(currentDateTime: DateTime): Future[List[KinopoiskLog]] = {
    collection.flatMap(
      _
        .find(
          BSONDocument(
            "created" -> BSONDocument(
              "$gte" -> (currentDateTime.getMillis / 1000).toInt
            )
          ),
          Option.empty[KinopoiskLog]
        )
        .cursor[KinopoiskLog](ReadPreference.Primary)
        .collect[List](err = Cursor.FailOnError[List[KinopoiskLog]]())
    )
  }

  def getLast(): Future[Option[KinopoiskLog]] = {
    collection.flatMap(
      _.find(BSONDocument(), Option.empty[KinopoiskLog])
        .sort(BSONDocument("created" -> -1))
        .one[KinopoiskLog]
    )
  }

  def getOlderWait(): Future[Option[KinopoiskLog]] = {
    collection.flatMap(
      _.find(
        BSONDocument("status" -> KinopoiskLogStatus.Wait.value),
        Option.empty[KinopoiskLog]
      )
        .sort(BSONDocument("created" -> 1))
        .one[KinopoiskLog]
    )
  }

  def insert(kinopoiskLog: KinopoiskLog): Future[WriteResult] = {
    collection.flatMap(
      _.insert(ordered = false).one(kinopoiskLog)
    )
  }
}
