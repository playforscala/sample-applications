name := "slick-crud"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  "com.typesafe.play" %% "play-slick" % "0.5.0.8",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick" % "1.0.1"
)

play.Project.playScalaSettings
