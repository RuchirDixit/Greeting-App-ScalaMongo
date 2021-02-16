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

import akka.http.scaladsl.model.{HttpEntity, MediaTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.ByteString
import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar

class GreetingAppRouteTest extends AnyWordSpec with should.Matchers with ScalatestRouteTest with MockitoSugar {
  "A Router" should {
    "register successfully" in {
      val jsonRequest = ByteString(
        s"""
           |{
           |    "msg":"Test",
           |    "name":"demo"
           |}
              """.stripMargin)
      Post("/message", HttpEntity(MediaTypes.`application/json`,jsonRequest)) ~>
        new UserManagementRoutes().route ~> check {
        assert(status == StatusCodes.OK)
      }
    }
  }
  "A Router" should {
    "register Json" in {
      val jsonRequest = ByteString(
        s"""
           |{
           |
           |}
              """.stripMargin)
      Get("/getJson", HttpEntity(MediaTypes.`application/json`,jsonRequest)) ~>
        new UserManagementRoutes().route ~> check {
        assert(status == StatusCodes.OK)
      }
    }
  }
  "A Router" should {
    "register XML" in {
      val jsonRequest = ByteString(
        s"""
           |{
           |
           |}
              """.stripMargin)
      Get("/getXML", HttpEntity(MediaTypes.`application/json`,jsonRequest)) ~>
        new UserManagementRoutes().route ~> check {
        assert(status == StatusCodes.OK)
      }
    }
  }
}
