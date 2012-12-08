import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "templates"
    val appVersion      = "0.1"

    val appDependencies = Seq(
      // Add your project dependencies here,
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here      
    )

}
