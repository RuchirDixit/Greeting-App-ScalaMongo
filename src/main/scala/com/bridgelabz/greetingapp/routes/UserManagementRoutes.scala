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
package com.bridgelabz.greetingapp.routes

import akka.Done
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.{Directives, ExceptionHandler, Route}
import com.bridgelabz.greetingapp.caseclasses.{Greeting, MyJsonProtocol}
import com.bridgelabz.greetingapp.database.{DatabaseService, MongoDAL}
import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver
import com.typesafe.scalalogging.LazyLogging
import scala.concurrent.{Future}
import scala.util.{Success}

class UserManagementRoutes extends LazyLogging with Directives with MyJsonProtocol{
  // Handling Arithmetic and Null Pointer Exceptions
  //$COVERAGE-OFF$
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
    case exception: Exception =>
      extractUri { uri =>
        logger.error(s"Request to $uri could not be handled normally")
        complete(HttpResponse(StatusCodes.BadRequest, entity = exception.toString))
      }
  }
  def route : Route =
    handleExceptions(myExceptionHandler){
      concat(
        /**
         * for saving messages and name
         * @input : It accepts message and name from body
         * @Return :  Data inserted on successful insertion
         */
        post {
          path("message") {
            entity(as[Greeting]) {
              emp =>
                val service = new DatabaseService
                val request: Future[Done] = service.sendRequest(emp)
                onComplete(request) {
                  _ =>
                    logger.info("Data inserted")
                    complete("Data Inserted!")
                }
            }
          }
        },
        /**
         * for getting messages in JSON format
         * @input : Fetches all the messages saved
         * @Return : Displays all the messages in json format
         */
        get {
          concat(
            path("getJson") {
              val greetingSeqFuture: Future[Seq[Greeting]] = MongoDAL.fetchAllGreetings()
              complete(greetingSeqFuture)
            },
            /**
             * for getting messages in XML format
             * @input : Fetches all the messages saved
             * @Return : Displays all the messages in XML format
             */
            path("getXML"){
              val greetingSeqFuture = MongoDAL.fetchAllGreetings()
              onComplete(greetingSeqFuture) {
                case Success(data) =>
                  val xStream = new XStream(new DomDriver())
                  val xml = xStream.toXML(data)
                  logger.info("XML data:" + xml)
                  complete(xml)
              }
            }
          )
        }
      )
    }
}
