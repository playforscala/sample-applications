name := "first-sample"

version := "1.0-SNAPSHOT"

updateOptions := updateOptions.value.withCachedResolution(true)

libraryDependencies += guice
libraryDependencies += "net.sf.barcode4j" % "barcode4j" % "2.1"

scalaVersion := "2.13.1"

routesGenerator := InjectedRoutesGenerator

lazy val root = (project in file(".")).enablePlugins(PlayScala)

/* play-bootstrap doesn't have Play 2.8 support yet
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  "com.adrianhurt" %% "play-bootstrap" % "1.5.1-P27-B4"
)
*/
