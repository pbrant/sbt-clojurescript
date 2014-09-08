package sbtclojurescript

import clojure.java.api.Clojure

import java.io.File

object Compiler {
  private def buildOptions(
    outputTo: String,
    outputDir: String,
    sourceMap: Option[String],
    additionalSettings: Option[String]
  ): Object = {
    val arrayMap = Clojure.`var`("clojure.core", "array-map")

    val options = arrayMap.invoke(
      Clojure.read(":output-to"), outputTo,
      Clojure.read(":output-dir"), outputDir
    )

    val sourceMapMap = sourceMap.map(
      s => arrayMap.invoke(Clojure.read(":source-map"), s)
    )

    val additionalSettingsMap = additionalSettings.map(
      s => Clojure.read(s)
    )

    val maps = List(Some(options), sourceMapMap, additionalSettingsMap).flatten.reverse

    val cons = Clojure.`var`("clojure.core", "cons")
    val args = maps.foldLeft(null: Object)((result, l) => cons.invoke(l, result))

    val merge = Clojure.`var`("clojure.core", "merge")

    merge.applyTo(args.asInstanceOf[clojure.lang.ISeq])
  }

  def run(
    inputDir: File,
    outputTo: File,
    outputDir: File,
    sourceMap: Option[File],
    additionalSettings: Option[String]
  ) {
    Thread.currentThread.setContextClassLoader(this.getClass.getClassLoader)

    outputTo.getParentFile.mkdirs()

    val require = Clojure.`var`("clojure.core", "require")
    require.invoke(Clojure.read("cljs.closure"))

    val options = buildOptions(
      outputTo.getAbsolutePath,
      outputDir.getAbsolutePath,
      sourceMap.map(_.getAbsolutePath),
      additionalSettings
    )

    val build = Clojure.`var`(Clojure.read("cljs.closure"), "build")

    build.invoke(inputDir.getAbsolutePath, options)
  }
}
