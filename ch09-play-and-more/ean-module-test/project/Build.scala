import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "ean-module-test"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "playforscala" %% "ean-module" % "1.0-SNAPSHOT"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
  )

}
