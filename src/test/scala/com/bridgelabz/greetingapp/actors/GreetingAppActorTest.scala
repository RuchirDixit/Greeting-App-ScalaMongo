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
package com.bridgelabz.greetingapp.actors

import akka.actor.Props
import akka.testkit.{ImplicitSender, TestProbe}
import com.typesafe.scalalogging.LazyLogging
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should
import org.scalatest.wordspec.{AnyWordSpec, AnyWordSpecLike}

import scala.concurrent.ExecutionContextExecutor

class GreetingAppActorTest extends
AnyWordSpecLike
with Matchers
with BeforeAndAfterAll with LazyLogging{
  implicit val system = ActorSystemFactory.system
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  "Save to database actor" must {
    "expect response" in {
      val probe = TestProbe()
      val saveToDatabaseActor = system.actorOf(Props[GreetingActor], "greetingActor")
      probe.send(saveToDatabaseActor,"hey")
      probe.expectNoMessage()
    }
  }
}
