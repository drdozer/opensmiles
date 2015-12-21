scalaVersion := "2.11.7"

name := "opensmiles"

organization := "uk.co.turingatemyhamster"

version := "0.0.1"

testFrameworks += new TestFramework("utest.runner.Framework")

libraryDependencies += "com.lihaoyi" %% "fastparse" % "0.3.4"
