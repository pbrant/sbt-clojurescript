import sbtclojurescript._
import SbtClojureScriptPlugin.ClojureScriptKeys

seq(SbtClojureScriptPlugin.cljsSettings:_*)

ClojureScriptKeys.additionalSettings := Some("""
{ :optimizations :whitespace }
""")
