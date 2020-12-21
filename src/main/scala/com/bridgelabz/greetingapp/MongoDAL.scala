package com.bridgelabz.greetingapp
import org.mongodb.scala.bson.codecs.Macros
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.bson.codecs.configuration.CodecRegistries
import org.mongodb.scala.{FindObservable, MongoClient, MongoCollection, MongoDatabase}

import scala.concurrent.Future

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


  // method to fetch entire data from mongodb database
  def fetchAllGreetings() : Future[Seq[Greeting]] = {
    greetingCollection.find().toFuture()
  }
}
