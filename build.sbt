name := "alleles"

version := "0.1"

scalaVersion := "2.12.6"

scalacOptions += "-Ypartial-unification"
libraryDependencies += "org.typelevel" %% "cats-core" % "1.3.1"
libraryDependencies += "org.typelevel" %% "cats-laws" % "1.3.1"

libraryDependencies ++= Seq (
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.5.16",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.16" % Test
)

libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.0" % "test"

lazy val compilerOptions = Seq(
  scalacOptions ++= Seq(
    "-Ypartial-unification"
  )
)

lazy val core = project.in(file("modules/core"))
  .settings(compilerOptions)
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
  .dependsOn(core)
  .settings(compilerOptions)
  .settings(
    libraryDependencies ++= Seq(
      
    )
  )

lazy val examples = project.in(file("modules/examples"))
  .dependsOn(core)
  .settings(compilerOptions)
  .settings(
    libraryDependencies ++= Seq(
    )
  )
