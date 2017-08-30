import play.sbt.PlayImport._
import sbt._

object Library {
  lazy val versionOfJson4s = "3.2.11"

  lazy val versionOfAkka = "2.4.1"

  lazy val akka = Seq(
    "com.typesafe.akka" %% "akka-actor" % versionOfAkka,
    "com.typesafe.akka" %% "akka-cluster" % versionOfAkka,
    "com.typesafe.akka" %% "akka-kernel" % versionOfAkka,
    "com.typesafe.akka" %% "akka-slf4j" % versionOfAkka,
    "com.typesafe.akka" %% "akka-contrib" % versionOfAkka,
    "com.typesafe.akka" %% "akka-testkit" % versionOfAkka
  )

  lazy val guice = Seq(
    "net.codingwell" %% "scala-guice" % "4.0.0",
    "com.google.inject" % "guice" % "4.0"
  )

  lazy val akkaMonitor = Seq(
    "io.github.junheng.akka" %% "akka-monitor" % "0.2-SNAPSHOT" withSources()
  )

  lazy val logs = Seq(
    "org.slf4j" % "jul-to-slf4j" % "1.7.7",
    "org.slf4j" % "log4j-over-slf4j" % "1.7.7",
    "ch.qos.logback" % "logback-classic" % "1.1.3"
  )

  lazy val json = Seq(
    "org.json4s" %% "json4s-jackson" % versionOfJson4s withSources(),
    "org.json4s" %% "json4s-ext" % versionOfJson4s withSources(),
    "com.jayway.restassured" % "json-path" % "2.4.0"
  )

  lazy val test = Seq(
    "org.scalatest" %% "scalatest" % "2.2.1" % "test" intransitive(),
    "org.scalatestplus" %% "play" % "1.4.0-M3" % "test" intransitive(),
    specs2 % Test intransitive(),
    "com.github.athieriot" %% "specs2-embedmongo" % "0.7.0"
  )

  lazy val reactiveMongo = Seq(
    "org.reactivemongo" % "reactivemongo_2.11" % "0.12.0"
  )

  lazy val reactiveMongoPlay = Seq(
    "org.reactivemongo" % "play2-reactivemongo_2.11" % "0.12.0-play24"
  )

}