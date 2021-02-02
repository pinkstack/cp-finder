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

mainClass in assembly := Some("com.pinkstack.Main")
assemblyJarName in assembly := "cp-finder.jar"

assemblyMergeStrategy in assembly := {
  case "module-info.class" => MergeStrategy.discard
  case x                   =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
assemblyOutputPath in assembly := file("cp-finder.jar")

scalacOptions ++= Seq(
  "-encoding", "UTF-8",
  "-feature",
  "-explaintypes",
  "-unchecked",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:postfixOps",
  "-Yrangepos",
  "-target:11"
)
