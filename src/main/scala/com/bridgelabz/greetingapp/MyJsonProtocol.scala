package com.bridgelabz.greetingapp

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait MyJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val templateFormat = jsonFormat2(Greeting)
}
