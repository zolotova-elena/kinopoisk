package ru.kinopoisk.daemon.models.movie

import reactivemongo.api.bson.BSONObjectID
import reactivemongo.api.bson.Macros.Annotations.Key

case class Movie(
  _id: BSONObjectID = BSONObjectID.generate(),
  page: Int,
  @Key("kinopoisk_log") kinopoiskLog: BSONObjectID,
  id: Int,
  @Key("id_kinopoisk") idKinopoisk: Int,
  url: String,
  `type`: String,
  title: String,
  @Key("title_alternative") titleAlternative: String,
  @Key("tagline") taglineO: Option[String] = None, // todo check
  @Key("description") descriptionO: Option[String] = None,
  year: Int,
  poster: String,
  trailer: String,
  @Key("age") ageO: Int, // todo check
  actors: List[String] = List.empty[String],
  countries: List[String] = List.empty[String],
  genres: List[String] = List.empty[String],
  directors: List[String] = List.empty[String],
  screenwriters: List[String] = List.empty[String],
  producers: List[String] = List.empty[String],
  operators: List[String] = List.empty[String],
  composers: List[String] = List.empty[String],
  painters: List[String] = List.empty[String],
  editors: List[String] = List.empty[String],
  //"budget": null,
  @Key("rating_kinopoisk") ratingKinopoiskO: Option[String] = None,
  @Key("rating_imdb") ratingImdbO: Option[String] = None,
  @Key("kinopoisk_votes") kinopoiskVotesO: Option[String] = None,
  @Key("imdb_votes") imdbVotesO: Option[String] = None,
  //"fees_world": null,
  //"fees_russia": null,
  @Key("premiere_world") premiereWorldO: Option[String] = None,
  @Key("premiere_russia") premiereRussiaO: Option[String] = None,
  frames: List[String] = List.empty[String],
  screenshots: List[String] = List.empty[String],
  videocdn: Quality = Quality(),
  hdvb: Quality = Quality(),
  collapse: Quality = Quality(),
  kodik: Quality = Quality()
)

// todo add reader
