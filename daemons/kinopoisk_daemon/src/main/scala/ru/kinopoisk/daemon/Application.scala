package ru.kinopoisk.daemon

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.http.scaladsl.server.Route
import ru.kinopoisk.daemon.modules.AppModule

object Application extends App with AppModule {
  logger.info("App is running")

  val kinopoiskUploader = kinopoiskUploaderWorker.value.run()
  val kinopoiskParser = kinopoiskParserWorker.value.run()

  val router: Route = path("health") {
    get {complete(OK)}
  }

  val bindingFuture = Http().newServerAt(appConfig.host, appConfig.port).bind(router)

  sys.addShutdownHook {
    logger.info("Terminating")
    kinopoiskUploader.cancel()
    kinopoiskParser.cancel()
    logger.info("Terminated")

    logger.info("HTTP Terminating")
    Await.result(bindingFuture, 60.seconds).terminate(hardDeadline = 30.seconds)

    daos.foreach(_.close(30.seconds))

    logger.info("system Terminating")
    system.terminate()
    logger.info("system Terminated")
  }
}
