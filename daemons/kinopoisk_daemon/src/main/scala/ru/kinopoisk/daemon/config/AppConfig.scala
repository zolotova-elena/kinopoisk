package ru.kinopoisk.daemon.config

import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.duration.{DurationLong, FiniteDuration}

class AppConfig {
  val config: Config = ConfigFactory.load()

  val host: String = config.getString("app.host")
  val port: Int = config.getInt("app.port")

  private val kinopoiskUploaderWorkerConfig = config.getConfig("kinopoisk_uploader_worker")

  val initialDelay: FiniteDuration = kinopoiskUploaderWorkerConfig.getDuration("initial_delay").toMillis.milliseconds
  val interval: FiniteDuration = kinopoiskUploaderWorkerConfig.getDuration("interval").toMillis.milliseconds

}
