import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "ean-module-plugin"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "net.sf.barcode4j" % "barcode4j" % "2.0"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    publishTo := Some(Resolver.file("Our repository",
      new File("/Users/paco/writing/playforscala.github.com"))),
    organization := "playforscala"
  )

}
