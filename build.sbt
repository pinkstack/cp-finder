name := "cp-finder"

version := "0.1"

scalaVersion := "2.13.4"

idePackagePrefix := Some("com.pinkstack")

libraryDependencies ++= {
  Dependencies.akka ++
    Dependencies.circe ++
    Dependencies.fp ++
    Dependencies.configurationLibs ++
    Dependencies.logging ++
    Dependencies.testing
}
