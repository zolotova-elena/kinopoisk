package ru.kinopoisk.daemon.models.movie

case class Quality(
  urlO: Option[String] = None,
  qualityO: Option[String] = None,
  durationO: Option[String] = None,
  voiceO: Option[String] = None
)

// todo add reader
