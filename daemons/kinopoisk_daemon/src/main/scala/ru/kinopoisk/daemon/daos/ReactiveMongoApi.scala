package ru.kinopoisk.daemon.daos

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}

import reactivemongo.api.{AsyncDriver, DB, MongoConnection}

class ReactiveMongoApi (
  mongoUri: String
)(implicit ex: ExecutionContext) {

  private val driver = AsyncDriver()

  def close(timeout: FiniteDuration): Future[Unit] = driver.close(timeout)

  val connection: Future[MongoConnection] = driver.connect(mongoUri)
  val db: Future[DB] = connection.flatMap(_.database("movies")) // todo
}
