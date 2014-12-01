name := "slick-lifted-embedding"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
    "com.typesafe.slick" %% "slick" % "1.0.1",
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    "postgresql" % "postgresql" % "9.1-901.jdbc4"
)

play.Project.playScalaSettings
