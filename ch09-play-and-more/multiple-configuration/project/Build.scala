import sbt._

object ApplicationBuild extends Build {

  val appName         = "multiple-configuration"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
  )

}
