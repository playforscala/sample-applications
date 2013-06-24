import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "squeryl-sample-stateless"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    jdbc,
    "net.sf.barcode4j" % "barcode4j" % "2.0",
    "org.squeryl" % "squeryl_2.9.0-1" % "0.9.4"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
  )

}
