import play.sbt.PlayImport.guice
import sbt.Keys.{libraryDependencies, name, scalaVersion, version}
import sbt._

object Modules {

  val AkkaVersion = "2.6.8"
  val AkkaHttpVersion = "10.2.9"

  lazy val daemonKinopoisk: Project => Project = project => {
    project
      .settings(
        name := """kinopoisk_daemon""",
        version := "0.1",
        scalaVersion := "2.13.6",
        libraryDependencies ++= Seq(
          guice,
          "joda-time" % "joda-time" % "2.10.10",
          "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
          "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
          "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
          "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion,
          "com.softwaremill.macwire" %% "macros" % "2.5.0" % "provided",
          "com.softwaremill.common" %% "tagging" % "2.2.1",
          "org.reactivemongo" %% "reactivemongo" % "1.0.5",
          "com.beachape" %% "enumeratum" % "1.7.0",
          "com.beachape" %% "enumeratum-reactivemongo-bson" % "1.7.0",
          //for logs
          "ch.qos.logback" % "logback-classic" % "1.2.3",
          "org.slf4j" % "slf4j-api" % "1.7.30"
        )
      )
  }
}
