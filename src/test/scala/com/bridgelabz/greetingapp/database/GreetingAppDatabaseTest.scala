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

import com.bridgelabz.greetingapp.actors.ActorSystemFactory
import com.bridgelabz.greetingapp.caseclasses.Greeting
import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success
class GreetingAppDatabaseTest extends AnyWordSpec with should.Matchers {
  val system = ActorSystemFactory.system
  implicit val executor: ExecutionContext = system.dispatcher
  "On Successful chat sent to group" should {
    "return Message exception group chat" in {
      val data = Greeting("Test","Name")
      val status = DatabaseService.sendRequest(data)
      status map { future => assert(future==true)}
    }
  }
  "On Successful chat sent to group" should {
    "return Message exception group chat 11" in {
      val status = DatabaseService.getJson()
      status map { future => assert(future==true)}
    }
  }

  "On Successful chat sent to group" should {
    "return Message exception group chat 1111" in {
      val status = MongoDAL.fetchAllGreetings()
      status map { future => assert(future==true)}
    }
  }
}
