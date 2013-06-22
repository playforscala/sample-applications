import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "slick-crud"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    jdbc,
    "com.typesafe.play" %% "play-slick" % "0.3.3",
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    "com.typesafe.slick" %% "slick" % "1.0.1"
  )

  val main = play.Project(appName, appVersion, appDependencies)
}
