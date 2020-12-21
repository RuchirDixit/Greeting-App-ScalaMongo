package com.bridgelabz.greetingapp

import akka.Done
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.{Directives, ExceptionHandler, Route}
import com.bridgelabz.greetingapp.DbConfig.sendRequest
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}
import com.thoughtworks.xstream._
import com.thoughtworks.xstream.io.xml.DomDriver
object Routes extends App with Directives with MyJsonProtocol{
  val host = sys.env("Host")
  val port = sys.env("Port_number").toInt
  implicit val system = ActorSystem("AS")
  var actor1 = system.actorOf(Props[GreetingActor],"actor1")
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
            path("getJson") {
              val greetingSeqFuture: Future[Seq[Greeting]] = MongoDAL.fetchAllGreetings()
              complete(greetingSeqFuture)
            },
            path("getXML"){
              val greetingSeqFuture = MongoDAL.fetchAllGreetings()
              val data = Await.result(greetingSeqFuture,10.seconds)
              val xStream = new XStream(new DomDriver())
              val xml = xStream.toXML(data)
              complete(xml)
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
