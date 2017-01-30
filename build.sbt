name := """air-master"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"



libraryDependencies ++= Seq(
  specs2 % Test,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.12.1",
  "org.webjars" % "bootstrap" % "3.3.7-1",
  "org.webjars" %% "webjars-play" % "2.5.0",
  "org.webjars" % "jquery-ui" % "1.9.2",
  "org.reactivemongo" %% "reactivemongo-iteratees" % "0.12.1",
  "com.typesafe.play" %% "play-iteratees" % "2.5.10",
  "org.jsoup" % "jsoup" % "1.7.2" % "test"
)
