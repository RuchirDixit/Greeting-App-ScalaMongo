package com.bridgelabz.greetingapp

import akka.Done
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Route
import com.bridgelabz.greetingapp.DbConfig.{as, complete, concat, entity, get, host, onComplete, path, port, post, sendRequest, system}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class Routes{
  implicit val executor: ExecutionContext = system.dispatcher
  def route : Route =
    concat(
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
      get {
        concat(
          path("getJson"){
            complete(Greeting("Holaaa Amigo","Pablo"))
          },
          path("getXML"){
            complete(HttpEntity(
              ContentTypes.`text/xml(UTF-8)`,
              "{\"msg\":\"Hiiiiii\",\"name\":\"Ruchirrrr\"}"
            ))
          }
        )
      }
    )
  val bindingFuture = Http().newServerAt(host,port).bind(route)
  bindingFuture.onComplete {
    case Success(serverBinding) => println(println(s"Listening to ${serverBinding.localAddress}"))
    case Failure(error) => println(s"Error : ${error.getMessage}")
  }
}
