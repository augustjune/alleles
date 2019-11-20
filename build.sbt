

lazy val commonSettings = Seq(
  version := "0.1",
  organization := "org.augustjune",
  scalaVersion := "2.13.1"
)

lazy val alleles = project.in(file("."))
  .settings(commonSettings)
  .dependsOn(core, benchmarks, examples)
  .aggregate(core, benchmarks, examples)

lazy val core = project.in(file("modules/core"))
  .settings(commonSettings)
  .settings(
    name := "alleles-core",
    libraryDependencies ++= Seq(
      "co.fs2"                  %% "fs2-core"             % "2.1.0",
      "org.typelevel"           %% "cats-core"            % "2.0.0",
      "org.typelevel"           %% "cats-laws"            % "2.0.0",
      "org.scalactic"           %% "scalactic"            % "3.0.8",
      "org.scalacheck"          %% "scalacheck"           % "1.14.2"  % Test
    )
  )

lazy val benchmarks = project.in(file("modules/benchmarks"))
  .dependsOn(core, examples)
  .settings(commonSettings)
  .settings(
    name := "alleles-benchmarks",
    libraryDependencies ++= Seq(
      "com.storm-enroute" %% "scalameter" % "0.19"
    )
  )

lazy val examples = project.in(file("modules/examples"))
  .dependsOn(core)
  .settings(commonSettings)
  .settings(
    name := "alleles-examples",

    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.0.8"
    )
  )
