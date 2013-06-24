import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "anorm-sample"
    val appVersion      = "1.0-SNAPSHOT"

    // #ch05-deps
    val appDependencies = Seq(
      jdbc,
      anorm,
      "postgresql" % "postgresql" % "9.1-901.jdbc4"
    )
    // #ch05-deps

    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here      
    )

}
