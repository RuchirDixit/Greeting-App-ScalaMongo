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

import akka.Done
import akka.actor.ActorSystem
import com.typesafe.scalalogging.LazyLogging
import scala.concurrent.Future
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object DbConfig extends LazyLogging {
  implicit val system = ActorSystem("HelloWorld")
  implicit val executor: ExecutionContext = system.dispatcher
  val mongoClient: MongoClient = MongoClient()
  // Getting mongodb database
  val database: MongoDatabase = mongoClient.getDatabase("mydb")
  // Getting mongodb collection
  val collection: MongoCollection[Document] = database.getCollection("test")
  collection.drop()

  /**
   *
   * @param greet : Data to be added into database
   * @return : Future[Done]
   */
  def sendRequest(greet: Greeting) : Future[Done.type] = {
    logger.info("Sending request with object: " + greet)
    val doc: Document = Document("msg" -> greet.msg, "name" -> greet.name)
    val bindFuture = collection.insertOne(doc).toFuture()
    bindFuture.onComplete {
      case Success(_) => logger.info("Successfully Added!")
      case Failure(exception) => logger.error(exception.toString)
    }
    Future{Done}
  }

  // returns data in form of Future from mongodb database
  def getJson() : Future[Seq[Document]] = {
    val future = collection.find().toFuture()
    future
  }
}
