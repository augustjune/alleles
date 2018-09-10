name := "allele"

version := "0.1"

scalaVersion := "2.12.6"

scalacOptions += "-Ypartial-unification"
libraryDependencies += "org.typelevel" %% "cats-core" % "1.3.1"

libraryDependencies ++= Seq (
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)
