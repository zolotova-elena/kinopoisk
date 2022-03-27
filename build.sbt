name := "kinopoisk"

version := "0.1"

scalaVersion := "2.13.8"

lazy val daemonKinopoisk =
  project.in(file("daemons/kinopoisk_daemon"))
    .enablePlugins(JavaAppPackaging)
    .configure(Modules.daemonKinopoisk)