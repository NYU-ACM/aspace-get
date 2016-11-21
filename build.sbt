scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.apache.httpcomponents" % "httpclient" % "4.5.2",
  "org.json4s" % "json4s-native_2.11" % "3.4.0",
  "com.typesafe" % "config" % "1.3.0"
  )

assemblyJarName in assembly := "asget.jar"

mainClass in assembly := Some("asget.Main")
