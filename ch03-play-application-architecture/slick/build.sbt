name := "slick"

version := "1.0"

scalaVersion := "2.10.0-RC1"

scalacOptions += "-deprecation"

libraryDependencies ++= List(
  "com.typesafe" % "slick_2.10.0-RC1" % "0.11.2",
  "ch.qos.logback" % "logback-classic" % "1.0.3",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
//  "org.slf4j" % "slf4j-api" % "1.6.4",
  "postgresql" % "postgresql" % "9.1-901.jdbc4"
)
