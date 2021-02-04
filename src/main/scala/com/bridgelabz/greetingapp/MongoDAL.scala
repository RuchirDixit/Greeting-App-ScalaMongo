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
package com.bridgelabz.greetingapp

import org.mongodb.scala.bson.codecs.Macros
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.bson.codecs.configuration.CodecRegistries
import org.mongodb.scala.{FindObservable, MongoClient, MongoCollection, MongoDatabase}
import scala.concurrent.Future

object MongoDAL {

  val greetingCodecProvider = Macros.createCodecProvider[Greeting]()

  val codecRegistry = CodecRegistries.fromRegistries(
    CodecRegistries.fromProviders(greetingCodecProvider),
    DEFAULT_CODEC_REGISTRY
  )

  val mongoClient: MongoClient = MongoClient()

  val mongoDatabase: MongoDatabase =
    mongoClient
      .getDatabase("mydb")
      .withCodecRegistry(codecRegistry)

  val greetingCollection: MongoCollection[Greeting] =
    mongoDatabase.getCollection[Greeting]("test")


  // method to fetch entire data from mongodb database
  def fetchAllGreetings() : Future[Seq[Greeting]] = {
    greetingCollection.find().toFuture()
  }
}
