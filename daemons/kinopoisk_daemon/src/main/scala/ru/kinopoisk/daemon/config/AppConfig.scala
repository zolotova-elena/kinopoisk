package ru.kinopoisk.daemon.config

import scala.concurrent.duration.{DurationLong, FiniteDuration}

import com.typesafe.config.{Config, ConfigFactory}

class AppConfig {
  val config: Config = ConfigFactory.load()

  val host: String = config.getString("app.host")
  val port: Int = config.getInt("app.port")

  private val kinopoiskUploaderWorkerConfig = config.getConfig("kinopoisk_uploader_worker")

  val initialDelay: FiniteDuration = kinopoiskUploaderWorkerConfig.getDuration("initial_delay").toMillis.milliseconds
  val interval: FiniteDuration = kinopoiskUploaderWorkerConfig.getDuration("interval").toMillis.milliseconds

  val mongoMovies: String = config.getString("mongodb.movies.uri")

  private val kinopoiskConfig: Config = config.getConfig("services.kinopoisk")

  val kinopoiskUri: String = kinopoiskConfig.getString("uri")
  val kinopoiskToken: String = kinopoiskConfig.getString("token")
  val maxRequestsInDay: Int = kinopoiskConfig.getInt("max-requests-in-day")
}
