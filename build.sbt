
lazy val scalaOptions = Seq(
  scalacOptions ++= Seq(
    "-Ypartial-unification"
  )
)

lazy val commonSettings = scalaOptions ++ Seq(
  version := "0.1",
  organization := "org.augustjune",
  scalaVersion := "2.12.6"
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
      "org.typelevel"         %% "cats-core"            % "1.3.1",
      "org.typelevel"         %% "cats-laws"            % "1.3.1",
      "com.typesafe.akka"     %% "akka-stream"          % "2.5.16",
      "org.scalactic"         %% "scalactic"            % "3.0.5",

      "com.typesafe.akka"     %% "akka-stream-testkit"  % "2.5.16"  % Test,
      "org.scalacheck"        %% "scalacheck"           % "1.14.0"  % Test
    )
  )

lazy val benchmarks = project.in(file("modules/benchmarks"))
  .dependsOn(core, examples)
  .settings(commonSettings)
  .settings(
    name := "alleles-benchmarks"
  )

lazy val examples = project.in(file("modules/examples"))
  .dependsOn(core)
  .settings(commonSettings)
  .settings(
    name := "alleles-examples",

    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.0.5"
    )
  )
