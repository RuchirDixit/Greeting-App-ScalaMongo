// Copyright (C) 2011-2012 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
name := "Greeting-App-ScalaMongo"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.6.8",
  "com.typesafe.akka" %% "akka-http" % "10.2.1",
  "com.typesafe.akka" %% "akka-stream" % "2.6.8",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.2.1" % Test,
  "ch.rasc" % "bsoncodec" % "1.0.1",
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.7.0",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.1",
  "com.typesafe.akka" %% "akka-http-xml" % "10.2.1",
  "com.thoughtworks.xstream" % "xstream" % "1.4.11.1",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.scalatest" %% "scalatest" % "3.2.2" % Test,
  "com.typesafe.akka" %% "akka-testkit" % "2.6.8" % Test,
  "org.scalatestplus" %% "mockito-3-4" % "3.2.3.0" % "test",
  "org.scoverage" %% "scalac-scoverage-runtime" % "1.4.0",
  "org.scalatestplus" %% "mockito-3-4" % "3.2.3.0" % "test"
)