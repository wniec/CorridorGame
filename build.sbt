ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.2"

lazy val root = (project in file("."))
  .settings(
    name := "Corridor"
  )
libraryDependencies += "org.scalafx" %% "scalafx" % "19.0.0-R30"
libraryDependencies += "org.diirt.javafx" % "javafx-all" % "3.1.7"