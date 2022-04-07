package ru.kinopoisk.daemon.workers.kinopoisk_parser

import javax.inject.Inject

import akka.actor.Cancellable
import akka.event.{Logging, LoggingAdapter}
import akka.stream.{ActorAttributes, Attributes, Supervision}
import akka.stream.scaladsl.RunnableGraph

class KinopoiskParserWorker @Inject()(
  source: KinopoiskParserSource,
  flow: KinopoiskParserFlow,
  sink: KinopoiskParserSink,
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
