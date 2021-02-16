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

import akka.Done
import com.bridgelabz.greetingapp.actors.ActorSystemFactory
import com.bridgelabz.greetingapp.caseclasses.Greeting
import com.typesafe.scalalogging.LazyLogging
import org.mongodb.scala.bson.collection.immutable.Document
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext
object DatabaseService extends LazyLogging {
  val system = ActorSystemFactory.system
  implicit val executor: ExecutionContext = system.dispatcher
  /**
   * Adds message and name to database and Future[Done]
   * @param greet : Data to be added into database
   * @return : Future[Done]
   */
  def sendRequest(greet: Greeting) : Future[Done.type] = {
    logger.info("Sending request with object: " + greet)
    val messageToBeAdded: Document = Document("msg" -> greet.msg, "name" -> greet.name)
    val bindFuture = MongoDAL.collection.insertOne(messageToBeAdded).toFuture()
    bindFuture.onComplete {
      case Success(_) => logger.info("Successfully Added!")
      case Failure(exception) => logger.error(exception.toString)
    }
    Future{Done}
  }

  /**
   * Fetches all the messages in Json format
   * @return : Future[Seq[Document]]
   */
  def getJson() : Future[Seq[Document]] = {
    val future = MongoDAL.collection.find().toFuture()
    future
  }
}
