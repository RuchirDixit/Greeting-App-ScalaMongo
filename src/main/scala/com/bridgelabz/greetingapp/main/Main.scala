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
package com.bridgelabz.greetingapp.main

import akka.actor.Props
import akka.http.scaladsl.Http
import com.bridgelabz.greetingapp.actors.{ActorSystemFactory, GreetingActor}
import com.bridgelabz.greetingapp.routes.UserManagementRoutes
import com.typesafe.scalalogging.LazyLogging
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}
// $COVERAGE-OFF$
object Main extends App with LazyLogging{
  private val host = sys.env("Host")
  private val port = sys.env("Port_number").toInt
  implicit val system = ActorSystemFactory.system
  var greetingActor = system.actorOf(Props[GreetingActor],"greetingActor")
  implicit val executor: ExecutionContext = system.dispatcher
  val routes = new UserManagementRoutes
  val route = routes.route
  val bindingFuture = Http().newServerAt(host,port).bind(route)
  bindingFuture.onComplete {
    case Success(serverBinding) => logger.info(s"Listening to ${serverBinding.localAddress}")
    case Failure(error) => logger.error(s"Error : ${error.getMessage}")
  }
}
