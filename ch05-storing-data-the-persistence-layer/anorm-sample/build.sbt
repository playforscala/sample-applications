name := "anorm-sample"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  "postgresql" % "postgresql" % "9.1-901.jdbc4"
)

play.Project.playScalaSettings
