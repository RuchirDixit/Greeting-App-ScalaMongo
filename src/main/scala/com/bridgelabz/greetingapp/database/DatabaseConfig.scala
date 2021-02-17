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
package com.bridgelabz.greetingapp.database

import akka.actor.ActorSystem
import com.bridgelabz.greetingapp.actors.ActorSystemFactory
import com.bridgelabz.greetingapp.caseclasses.Greeting
import org.mongodb.scala.bson.codecs.Macros
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.bson.codecs.configuration.CodecRegistries
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase}

import scala.concurrent.{ExecutionContext, Future}

trait DatabaseConfig {

  implicit val system = ActorSystemFactory.system
  implicit val executor: ExecutionContext = system.dispatcher
  val host = sys.env("HOST")
  val port = sys.env("MONGOPORT")
  val url = s"mongodb://${host}:${port}"
  // MongoClient connection
  val mongoClient: MongoClient = MongoClient(url)

  val greetingCodecProvider = Macros.createCodecProvider[Greeting]()

  val codecRegistry = CodecRegistries.fromRegistries(
    CodecRegistries.fromProviders(greetingCodecProvider),
    DEFAULT_CODEC_REGISTRY
  )

  // Getting mongodb database
  val mongoDatabase: MongoDatabase =
    mongoClient
      .getDatabase(sys.env("DATABASENAME"))
      .withCodecRegistry(codecRegistry)

  // Getting mongodb collection
  val greetingCollection: MongoCollection[Greeting] =
    mongoDatabase.getCollection[Greeting]("greets")

}
