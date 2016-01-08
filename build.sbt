scalaVersion := "2.11.7"

name := "opensmiles"

organization := "uk.co.turingatemyhamster"

version := "0.0.1"

testFrameworks += new TestFramework("utest.runner.Framework")

libraryDependencies += "com.lihaoyi" %% "fastparse" % "0.3.4"

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>https://github.com/drdozer/opensmiles</url>
  <licenses>
    <license>
      <name>Apache License, Version 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>https://github.com/drdozer/opensmiles.git</url>
    <connection>scm:git:git@github.com:drdozer/opensmiles.git</connection>
  </scm>
  <developers>
    <developer>
      <id>drdozer</id>
      <name>Matthew Pocock</name>
      <url>http://turingatemyhamster.co.uk/blog</url>
    </developer>
  </developers>
)
