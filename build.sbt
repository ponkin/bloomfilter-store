lazy val root = (project in file(".")).
  settings(
    name := "bloomfilter-store",
    version := "1.0",
    scalaVersion := "2.11.8"
  )

enablePlugins(JavaAppPackaging)

enablePlugins(DockerPlugin)

libraryDependencies ++= Seq(
  "com.twitter" %% "finagle-redis" % "6.39.0",
  "com.twitter" %% "algebird-core" % "0.12.2"
)
