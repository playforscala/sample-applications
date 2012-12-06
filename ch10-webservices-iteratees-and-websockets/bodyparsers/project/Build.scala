import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "bodyparsers"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      // Async-http-client in Play 2.0.3 has a bug preventing S3 uploader to fail.  
      "com.ning" % "async-http-client" % "1.7.5",
      // Required for the FeedableBodyGenerator used by S3 uploader.
      "org.glassfish.grizzly" % "grizzly-http-client" % "1.1"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here      
    )

}
