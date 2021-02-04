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
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.{Directives, ExceptionHandler, Route}
import com.bridgelabz.greetingapp.DbConfig.sendRequest
import com.bridgelabz.greetingapp.caseclasses.MyJsonProtocol

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}
import com.thoughtworks.xstream._
import com.thoughtworks.xstream.io.xml.DomDriver
import com.typesafe.scalalogging.LazyLogging
object Routes extends App with Directives with MyJsonProtocol with LazyLogging{
  val host = sys.env("Host")
  val port = sys.env("Port_number").toInt
  implicit val system = ActorSystem("AS")
  var actor1 = system.actorOf(Props[GreetingActor],"actor1")
  implicit val executor: ExecutionContext = system.dispatcher
  // Handling Arithmetic and Null Pointer Exceptions
  val myExceptionHandler = ExceptionHandler {
    case _: ArithmeticException =>
      extractUri { uri =>
        logger.error(s"Request to $uri could not be handled normally")
        complete(HttpResponse(StatusCodes.BadRequest, entity = "Bad numbers, bad result!!!"))
      }
    case _: NullPointerException =>
      extractUri { uri =>
        logger.error(s"Request to $uri could not be handled normally")
        complete(HttpResponse(StatusCodes.BadRequest, entity = "Null value found!!!"))
      }
  }
  // Define routing for path /message or /getJson ; /getXML
  def route : Route =
    handleExceptions(myExceptionHandler){
      concat(
        // post method since we need to add data
        post {
          path("message") {
            entity(as[Greeting]) {
              emp =>
                val request: Future[Done] = sendRequest(emp)
                onComplete(request) {
                  _ => complete("Data Inserted!")
                }
            }
          }
        },
        // get request to fetch data
        get {
          concat(
            path("getJson") {
              val greetingSeqFuture: Future[Seq[Greeting]] = MongoDAL.fetchAllGreetings()
              complete(greetingSeqFuture)
            },
            path("getXML"){
              val greetingSeqFuture = MongoDAL.fetchAllGreetings()
              val data = Await.result(greetingSeqFuture,10.seconds)
              val xStream = new XStream(new DomDriver())
              val xml = xStream.toXML(data)
              logger.info("XML data:" + xml)
              complete(xml)
            }
          )
        }
      )
    }
  val bindingFuture = Http().newServerAt(host,port).bind(route)
  bindingFuture.onComplete {
    case Success(serverBinding) => logger.info(s"Listening to ${serverBinding.localAddress}")
    case Failure(error) => logger.error(s"Error : ${error.getMessage}")
  }
}
