name := "hello"

version := "0.1"

scalaVersion := "2.13.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies += guice
libraryDependencies += "net.sf.barcode4j" % "barcode4j" % "2.1"
