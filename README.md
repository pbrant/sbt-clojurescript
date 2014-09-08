# sbt-clojurescript

[Simple Build Tool](http://www.scala-sbt.org/ "simple build tool") plugin for compiling JavaScript files from multiple sources using ClojureScript.

## Settings


## Installation

If you have not already added the sbt community plugin resolver to your plugin definition file, add this

    resolvers += Resolver.url("sbt-plugin-releases",
      new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/"))(
        Resolver.ivyStylePatterns)

Then add this (see [ls.implicit.ly](http://ls.implicit.ly/pbrant/sbt-clojurescript#sbt-clojurescript) for current version)

    addSbtPlugin("org.scala-sbt" % "sbt-clojurescript" % "0.1.0")

Then in your build definition, add

    seq(cljsSettings:_*)

This will append `sbt-clojurescript`'s settings for the `Compile` and `Test` configurations.

To add them to other configurations, use the provided `cljsSettingsIn(config)` method.

    seq(cljsSettingsIn(SomeOtherConfig):_*)

## Usage

The plugin scans your `src/main/cljs` directory
and looks for files of extension `.cljs`.

## Tasks

The plugin is tied to the compile task and will run whenver compile is run. You can
also run `cljs` to run it on its own. `clean(for cljs)` will delete the generated files.

## Customization

If you're using [xsbt-web-plugin](https://github.com/JamesEarlDouglas/xsbt-web-plugin "xsbt-web-plugin"), add the output files to the webapp with:

    // add managed resources to the webapp
    (webappResources in Compile) <+= (resourceManaged in Compile)

### Changing the directory that is scanned, use:

    (sourceDirectory in (Compile, ClojureScriptKeys.closure)) <<= (sourceDirectory in Compile)(_ / "path" / "to" / "cljsfiles")

### Changing target js destination:

To change the default location of compiled js files, add the following to your build definition

    (resourceManaged in (Compile, ClojureScriptKeys.closure)) <<= (resourceManaged in Compile)(_ / "your_preference" / "js")

## Acknowledgements

This plugin is basically a hack of
[sbt-closure](https://github.com/eltimn/sbt-closure) which in turn contains the
following acknowledgments:

This plugin is a sbt 0.11.2 port of
[sbt-closure](https://github.com/davegurnell/sbt-closure)

It was modeled after and heavily influenced by [less-sbt](https://github.com/softprops/less-sbt "less-sbt")
