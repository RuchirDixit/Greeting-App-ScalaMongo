package com.bridgelabz.greetingapp.database

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext
object DatabaseService extends LazyLogging {
  /**
   * Adds message and name to database and Future[Done]
   * @param greet : Data to be added into database
   * @return : Future[Done]
   */
  def sendRequest(greet: Greeting) : Future[Done.type] = {
    logger.info("Sending request with object: " + greet)
    val doc: Document = Document("msg" -> greet.msg, "name" -> greet.name)
    val bindFuture = DbConfig.collection.insertOne(doc).toFuture()
    bindFuture.onComplete {
      case Success(_) => logger.info("Successfully Added!")
      case Failure(exception) => logger.error(exception.toString)
    }
    Future{Done}
  }

  // returns data in form of Future from mongodb database
  def getJson() : Future[Seq[Document]] = {
    val future = collection.find().toFuture()
    future
  }
}