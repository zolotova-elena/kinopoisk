package ru.kinopoisk.daemon.modules

import scala.concurrent.ExecutionContextExecutor

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.stream.Materializer
import com.softwaremill.macwire.{wire, wireSet}
import com.softwaremill.tagging.Tagger
import ru.kinopoisk.daemon.config.AppConfig
import ru.kinopoisk.daemon.daos.{KinopoiskLogDAO, ReactiveMongoApi}
import ru.kinopoisk.daemon.kinopoisk_uploader.{KinopoiskUploaderFlow, KinopoiskUploaderSink, KinopoiskUploaderSource, KinopoiskUploaderWorker}
import ru.kinopoisk.daemon.services.{KinopoiskLogService, KinopoiskService}

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

  //daos
  lazy val daos = wireSet[ReactiveMongoApi]
  lazy val kinopoiskLogDAO = wire[KinopoiskLogDAO]

  //service
  lazy val kinopoiskLogService: KinopoiskLogService = wire[KinopoiskLogService]
  lazy val kinopoiskService: KinopoiskService = wire[KinopoiskService]
}

trait Movies