import sbt._
import Keys._
import sbtassembly.AssemblyPlugin.autoImport._

name := "Docs2Words"
version := "0.1-SNAPSHOT"
organization := "uit.islab"
scalaVersion := "2.10.5"

unmanagedBase <<= baseDirectory { base => base / "libs" }

scalacOptions += "-deprecation"
assemblyJarName in assembly := name.value+"-"+version.value+".jar"
mainClass in assembly := Some("main.scala.Docs2Words")
test in assembly := {}