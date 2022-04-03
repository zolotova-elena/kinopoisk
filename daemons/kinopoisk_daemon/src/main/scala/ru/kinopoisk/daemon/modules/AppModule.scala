package ru.kinopoisk.daemon.modules

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.stream.Materializer
import com.softwaremill.macwire.wire
import ru.kinopoisk.daemon.config.AppConfig
import ru.kinopoisk.daemon.kinopoisk_uploader.{KinopoiskUploaderFlow, KinopoiskUploaderSink, KinopoiskUploaderSource, KinopoiskUploaderWorker}

import scala.concurrent.ExecutionContextExecutor

trait AppModule {

  lazy val appConfig: AppConfig = wire[AppConfig]

  implicit val system: ActorSystem = ActorSystem("kinopoisk")
  implicit val ex: ExecutionContextExecutor = system.dispatcher
  lazy val logger: LoggingAdapter = Logging(system, "kinopoisk")

  implicit val mat: Materializer = Materializer(system)

  lazy val kinopoiskUploaderFlow = wire[KinopoiskUploaderFlow]
  lazy val kinopoiskUploaderSink = wire[KinopoiskUploaderSink]
  lazy val kinopoiskUploaderSource = wire[KinopoiskUploaderSource]
  lazy val kinopoiskUploaderWorker = wire[KinopoiskUploaderWorker]
}
