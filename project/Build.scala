import sbt._
import sbt.Keys._

object Build extends sbt.Build {

  lazy val project = Project(
    id = "priority-set",
    base = file("."),
    settings = Defaults.defaultSettings ++ testSettings ++ Seq[Setting[_]](
      name := "Priority Set",
      version := "1.2-SNAPSHOT",
      description := """
        A mutable Java collection containing unique elements with priority-based ordering.
      """,
      scalaVersion := "2.10.0",
      javacOptions ++= Seq("-source", "1.6"),
      javacOptions in compile ++= Seq("-target", "1.6"),
      libraryDependencies += "com.google.code.findbugs" % "jsr305" % "1.3.9",
      organization := "org.codeswarm",
      organizationHomepage := Some(url("https://github.com/codeswarm")),
      homepage := Some(url("https://github.com/codeswarm/priority-set")),
      licenses := Seq(
        "The Apache Software License, Version 2.0" ->
        url("http://www.apache.org/licenses/LICENSE-2.0.txt")
      ),
      publishMavenStyle := true,
      publishTo <<= version { (v: String) =>
        val nexus = "https://oss.sonatype.org/"
        Some(
        if (v.trim.endsWith("SNAPSHOT"))
          "snapshots" at nexus + "content/repositories/snapshots"
        else
          "releases" at nexus + "service/local/staging/deploy/maven2"
        )
      },
      pomExtra := {
        val org = "codeswarm"
        val repo = "priority-set"
        <scm>
          <url>https://github.com/{org}/{repo}</url>
          <connection>scm:git:git://github.com/{org}/{repo}.git</connection>
          <developerConnection>scm:git:ssh://git@github.com/{org}/{repo}.git</developerConnection>
        </scm>
        <developers>
          <developer>
            <id>chris-martin</id>
            <name>Chris Martin</name>
            <url>https://github.com/chris-martin</url>
          </developer>
        </developers>
      }
    )
  )

  lazy val testSettings: Seq[Setting[_]] = {
    val framework = new TestFramework("com.dadrox.sbt.junit.JunitFramework")
    Seq(
      testFrameworks += framework,
      testOptions in Test += Tests.Argument(framework, "-vo", "-tv"),
      libraryDependencies ++= Seq(
        "com.dadrox" % "sbt-junit" % "0.3",
        "junit" % "junit" % "4.11",
        "com.google.guava" % "guava-testlib" % "13.0.1"
      ).map(_ % "test")
    )
  }

}
