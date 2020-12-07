package com.bridgelabz.greetingapp
import akka.Done
import com.bridgelabz.greetingapp.DbConfig.collection
import org.mongodb.scala.bson.codecs.Macros
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.bson.codecs.configuration.CodecRegistries
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase}

import scala.concurrent.Future
import scala.util.{Failure, Success}

object MongoDAL {

  val greetingCodecProvider = Macros.createCodecProvider[Greeting]()

  val codecRegistry = CodecRegistries.fromRegistries(
    CodecRegistries.fromProviders(greetingCodecProvider),
    DEFAULT_CODEC_REGISTRY
  )

  val mongoClient: MongoClient = MongoClient()

  val mongoDatabase: MongoDatabase =
    mongoClient
      .getDatabase("mydb")
      .withCodecRegistry(codecRegistry)

  val greetingCollection: MongoCollection[Greeting] =
    mongoDatabase.getCollection[Greeting]("test")

  def fetchAllGreetings() = {
    greetingCollection.find().toFuture()
  }
}
