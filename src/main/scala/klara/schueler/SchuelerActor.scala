package klara.schueler

import akka.actor._
import akka.pattern.pipe

import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.bson.handlers.BSONReader
import reactivemongo.bson.handlers.DefaultBSONHandlers.{DefaultBSONDocumentWriter,DefaultBSONReaderHandler}

import language.postfixOps

import klara.mongo.MongoUsingActor


case class FindAll()


class SchuelerActor extends MongoUsingActor {

  override def preStart =  {
    log.info("SchuelerActor started at: {}", self.path)
  }

  val collection = db("schueler")

  def receive = {
    case FindAll() => findAll()
  }

  def findAll() = {
    log.debug("finding all schueler")

    implicit val reader = Schueler.BSONReader

    val query = BSONDocument()

    collection.find(query).toList pipeTo sender
  }

}