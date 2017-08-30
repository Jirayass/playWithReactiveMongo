import sbt.Keys._

name := "face-service"

organization in ThisBuild := "com.remarkmedia"

version in ThisBuild := "1.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.11.6"

scalacOptions in ThisBuild := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Library.akka
libraryDependencies ++= Library.guice
libraryDependencies ++= Library.akkaMonitor
libraryDependencies ++= Library.reactiveMongoPlay
libraryDependencies ++= Library.json
libraryDependencies ++= Library.test

routesGenerator := play.routes.compiler.InjectedRoutesGenerator
