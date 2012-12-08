import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "response"
    val appVersion      = "1.0"

    // #ch4-response-binary-dependency
    val appDependencies = Seq(
      "net.sf.barcode4j" % "barcode4j" % "2.0"
    )
    // #ch4-response-binary-dependency

    val main = PlayProject(appName, appVersion, appDependencies).settings(
      // Add your own project settings here      
    )

}
