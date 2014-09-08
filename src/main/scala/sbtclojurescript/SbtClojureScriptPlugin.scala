package sbtclojurescript

import sbt._
import sbt.Project.Initialize

object SbtClojureScriptPlugin extends Plugin {
  import sbt.Keys._
  import ClojureScriptKeys._

  object ClojureScriptKeys {
    lazy val cljs = TaskKey[Seq[File]]("cljs", "Compiles ClojureScript source files")

    lazy val outputTo = SettingKey[File]("suffix", "The path to the output JavaScript file")
    lazy val sourceMap = SettingKey[Option[File]]("sourceMap", "The source map path")
    lazy val outputDir = SettingKey[File]("outputDir", "The temporary output directory")
    lazy val additionalSettings = SettingKey[Option[String]]("additionalSettings", "Additional compiler settings in EDN format")
  }

  def cljsSettings: Seq[Setting[_]] =
    cljsSettingsIn(Compile) ++ cljsSettingsIn(Test)

  def cljsSettingsIn(conf: Configuration): Seq[Setting[_]] =
    inConfig(conf)(cljsSettings0 ++ Seq(
      sourceDirectory in cljs <<= (sourceDirectory in conf) { _ / "cljs" },
      resourceManaged in cljs <<= (resourceManaged in conf) { _ / "js" },
      cleanFiles in cljs <<= (resourceManaged in cljs, outputDir in cljs)(_ :: _ :: Nil),
      watchSources <<= (unmanagedSources in cljs),
      outputTo := (resourceManaged in cljs).value / "main.js",
      outputDir := (target in cljs).value / "cljs"
    )) ++ Seq(
      additionalSettings := None,
      cleanFiles <++= (cleanFiles in cljs in conf),
      watchSources <++= (watchSources in cljs in conf),
      resourceGenerators in conf <+= cljs in conf,
      compile in conf <<= (compile in conf).dependsOn(cljs in conf)
    )

  def cljsSettings0: Seq[Setting[_]] = Seq(
    sourceMap := None,
    includeFilter in cljs := "*.cljs",
    excludeFilter in cljs := (".*" - ".") || HiddenFileFilter,
    unmanagedSources in cljs <<= cljsSourcesTask,
    clean in cljs <<= cljsCleanTask,
    cljs <<= cljsCompilerTask
  )

  private def cljsCleanTask =
    (streams, resourceManaged in cljs, outputDir in cljs) map {
      (out, target, outTemp) =>
        out.log.info("Cleaning generated JavaScript under " + target)
        IO.delete(target)

        out.log.info("Cleaning generated temporary JavaScript files under " + outTemp)
        IO.delete(outTemp)
    }

  private def cljsCompilerTask =
    (streams, sourceDirectory in cljs, resourceManaged in cljs,
     includeFilter in cljs, excludeFilter in cljs,
     outputTo in cljs, sourceMap in cljs, outputDir in cljs, additionalSettings in cljs) map {
      (out, sources, target, include, exclude, outTo, sMap, outDir, additional) => {
        val files = sources.descendantsExcept(include, exclude).get
        if (files.nonEmpty) {
          out.log.info(s"Compiling ${files.length} ClojureScript files to $outTo")
          Compiler.run(sources, outTo, outDir, sMap, additional)
        }
        compiled(target)
      }
    }

  private def cljsSourcesTask =
    (sourceDirectory in cljs, includeFilter in cljs, excludeFilter in cljs) map {
      (sourceDir, incl, excl) =>
         sourceDir.descendantsExcept(incl, excl).get
    }

  private def compiled(under: File) = (under ** "*.js").get
}
