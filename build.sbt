sbtPlugin := true

organization := "org.scala-sbt"

name := "sbt-clojurescript"

version <<= sbtVersion(v =>
  if(v.startsWith("0.13")) "0.1.0-SNAPSHOT"
  else error("unsupported sbt version %s" format v)
)

libraryDependencies += "org.clojure" % "clojure" % "1.6.0"

libraryDependencies += "org.clojure" % "clojurescript" % "0.0-2322"

seq(scriptedSettings:_*)

seq(lsSettings:_*)

(LsKeys.tags in LsKeys.lsync) := Seq("sbt", "clojurescript")

(description in LsKeys.lsync) :=
  "Sbt plugin for compiling ClojureScript sources"

homepage := Some(url("https://github.com/pbrant/sbt-clojurescript"))

publishTo := Some(Resolver.url("publishTo", new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns))

publishMavenStyle := false

publishArtifact in Test := false

licenses in GlobalScope += "Apache License 2.0" -> url("https://github.com/pbrant/sbt-clojurescript/raw/master/LICENSE")

pomExtra := (
  <scm>
    <url>git@github.com:pbrant/sbt-clojurescript.git</url>
    <connection>scm:git:git@github.com:pbrant/sbt-clojurescript.git</connection>
  </scm>
  <developers>
    <developer>
      <id>pbrant</id>
      <name>Peter Brant</name>
      <url>http://github.com/pbrant</url>
    </developer>
  </developers>
)

scalacOptions := Seq("-deprecation", "-unchecked", "-encoding", "utf8")
