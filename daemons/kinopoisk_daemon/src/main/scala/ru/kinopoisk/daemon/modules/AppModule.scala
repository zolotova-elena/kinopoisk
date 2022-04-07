package ru.kinopoisk.daemon.modules

import scala.concurrent.ExecutionContextExecutor

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.stream.Materializer
import com.softwaremill.macwire.{wire, wireSet}
import com.softwaremill.tagging.Tagger
import ru.kinopoisk.daemon.config.AppConfig
import ru.kinopoisk.daemon.daos.{KinopoiskLogDAO, ReactiveMongoApi}
import ru.kinopoisk.daemon.workers.kinopoisk_uploader._
import ru.kinopoisk.daemon.services.{KinopoiskLogService, KinopoiskService}
import ru.kinopoisk.daemon.workers.kinopoisk_parser._

trait AppModule {

  lazy val appConfig: AppConfig = wire[AppConfig]

  implicit val system: ActorSystem = ActorSystem("kinopoisk")
  implicit val ex: ExecutionContextExecutor = system.dispatcher
  lazy val logger: LoggingAdapter = Logging(system, "kinopoisk")

  implicit val mat: Materializer = Materializer(system)

  lazy val moviesMongoApi = new ReactiveMongoApi(appConfig.mongoMovies).taggedWith[Movies]

  // workers
  lazy val kinopoiskUploaderFlow = wire[KinopoiskUploaderFlow]
  lazy val kinopoiskUploaderSink = wire[KinopoiskUploaderSink]
  lazy val kinopoiskUploaderSource = wire[KinopoiskUploaderSource]
  lazy val kinopoiskUploaderWorker = wire[KinopoiskUploaderWorker]

  lazy val kinopoiskParserFlow = wire[KinopoiskParserFlow]
  lazy val kinopoiskParserSink = wire[KinopoiskParserSink]
  lazy val kinopoiskParserSource = wire[KinopoiskParserSource]
  lazy val kinopoiskParserWorker = wire[KinopoiskParserWorker]

  //daos
  lazy val daos = wireSet[ReactiveMongoApi]
  lazy val kinopoiskLogDAO = wire[KinopoiskLogDAO]

  //service
  lazy val kinopoiskLogService: KinopoiskLogService = wire[KinopoiskLogService]
  lazy val kinopoiskService: KinopoiskService = wire[KinopoiskService]
}

trait Movies