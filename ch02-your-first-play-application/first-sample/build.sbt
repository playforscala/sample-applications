name := "first-sample"

version := "1.0-SNAPSHOT"

updateOptions := updateOptions.value.withCachedResolution(true)

libraryDependencies += "net.sf.barcode4j" % "barcode4j" % "2.0"

scalaVersion := "2.11.8"

routesGenerator := InjectedRoutesGenerator

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  "com.adrianhurt" %% "play-bootstrap" % "1.1-P25-B3-SNAPSHOT"
)
