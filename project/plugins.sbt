resolvers += Resolver.url("scalasbt", new URL(
  "http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"))(
  Resolver.ivyStylePatterns)

libraryDependencies <+= sbtVersion { v =>
  "org.scala-sbt" % "scripted-plugin" % v
}

addSbtPlugin("me.lessis" % "ls-sbt" % "0.1.3")

