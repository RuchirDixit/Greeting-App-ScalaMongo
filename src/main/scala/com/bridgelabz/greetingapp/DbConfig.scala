package com.bridgelabz.greetingapp

import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives
import scala.concurrent.Future
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase}
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object DbConfig  {
  implicit val system = ActorSystem("HelloWorld")
  implicit val executor: ExecutionContext = system.dispatcher
  val mongoClient: MongoClient = MongoClient()
  val database: MongoDatabase = mongoClient.getDatabase("mydb")
  val collection: MongoCollection[Document] = database.getCollection("test")
  collection.drop()

  def sendRequest(greet: Greeting) = {
    val doc: Document = Document("msg" -> greet.msg, "name" -> greet.name)
    val bindFuture = collection.insertOne(doc).toFuture()
    bindFuture.onComplete {
      case Success(_) => println("Successfully Added!")
      case Failure(exception) => println(exception)
    }
    Future{Done}
  }
}
