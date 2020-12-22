name := "Greeting-App-ScalaMongo"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.6.8",
  "com.typesafe.akka" %% "akka-http" % "10.2.1",
  "com.typesafe.akka" %% "akka-stream" % "2.6.8",
  "ch.rasc" % "bsoncodec" % "1.0.1",
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.7.0",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.1",
  "com.typesafe.akka" %% "akka-http-xml" % "10.2.1",
  "com.thoughtworks.xstream" % "xstream" % "1.4.11.1",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)