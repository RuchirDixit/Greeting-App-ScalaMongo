package com.bridgelabz.greetingapp

import akka.Done
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.{Directives, ExceptionHandler, Route}
import com.bridgelabz.greetingapp.DbConfig.{sendRequest, system}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object Routes extends App with Directives with MyJsonProtocol with MyXMLSupport{
  val host = sys.env.getOrElse("HOST_ADDRESS", "0.0.0.0")
  val port = 9000
  implicit val executor: ExecutionContext = system.dispatcher
  // Handling Arithmetic and Null Pointer Exceptions
  val myExceptionHandler = ExceptionHandler {
    case _: ArithmeticException =>
      extractUri { uri =>
        println(s"Request to $uri could not be handled normally")
        complete(HttpResponse(400, entity = "Bad numbers, bad result!!!"))
      }
    case _: NullPointerException =>
      extractUri { uri =>
        println(s"Request to $uri could not be handled normally")
        complete(HttpResponse(404, entity = "Null value found!!!"))
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
    }
  val bindingFuture = Http().newServerAt(host,port).bind(route)
  bindingFuture.onComplete {
    case Success(serverBinding) => println(println(s"Listening to ${serverBinding.localAddress}"))
    case Failure(error) => println(s"Error : ${error.getMessage}")
  }
}
