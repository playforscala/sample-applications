import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "hello"
  val appVersion      = "0.1"

  val appDependencies = Seq(

  )

  val main = play.Project(appName, appVersion, appDependencies).settings(

  )

}
