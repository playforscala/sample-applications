name := "squeryl-sample-stateless"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  "net.sf.barcode4j" % "barcode4j" % "2.0",
  "org.squeryl" % "squeryl_2.9.0-1" % "0.9.4"
)

play.Project.playScalaSettings
