package com.bridgelabz.greetingapp

import akka.actor.{Actor, ActorLogging}
import com.bridgelabz.greetingapp.DbConfig.getJson

class GreetingActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case _ => log.info("Inside default")
              sender() ! getJson()
  }
}
