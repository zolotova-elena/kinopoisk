import play.sbt.PlayImport.guice
import sbt.Keys.{libraryDependencies, name, scalaVersion, version}
import sbt._

object Modules {

  lazy val daemonKinopoisk: Project => Project = project => {
    project
      .settings(
        name := """kinopoisk_daemon""",
        version := "0.1",
        scalaVersion := "2.13.6",
        libraryDependencies ++= Seq(
          guice,
          "joda-time" % "joda-time" % "2.10.10",
          "com.typesafe.akka" %% "akka-stream" % "2.6.3",
          "de.heikoseeberger" %% "akka-http-play-json" % "1.31.0"
        )
      )
  }
}
