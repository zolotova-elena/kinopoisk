package ru.kinopoisk.daemon.kinopoisk_uploader

import javax.inject.Inject

import akka.actor.Cancellable
import akka.event.{Logging, LoggingAdapter}
import akka.stream.scaladsl.RunnableGraph
import akka.stream.{ActorAttributes, Attributes, Supervision}

class KinopoiskUploaderWorker @Inject()(
  source: KinopoiskUploaderSource,
  flow: KinopoiskUploaderFlow,
  sink: KinopoiskUploaderSink,
  log: LoggingAdapter
) {

  private val decider: Supervision.Decider = { error =>
    log.info(s"[error] ${error.toString}")
    Supervision.Restart
  }

  val value: RunnableGraph[Cancellable] = {
    source.value
      .via(flow.value)
      .to(sink.value)
      .withAttributes(
        Attributes
          .logLevels(Logging.InfoLevel)
          .and(ActorAttributes.supervisionStrategy(decider))
      )
  }
}
