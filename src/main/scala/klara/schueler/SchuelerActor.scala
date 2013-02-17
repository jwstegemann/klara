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
case class Create(item: Schueler)
case class Load(id: String)
case class Update(item: Schueler)
case class Delete(id: String)


class SchuelerActor extends MongoUsingActor {

  override def preStart =  {
    log.info("SchuelerActor started at: {}", self.path)
  }

  val collection = db("schueler")

  def receive = {
    case FindAll() => findAll()
    case Create(item) => create(item)
    case Load(id) => load(id)
    case Update(item) => update(item)
    case Delete(id) => delete(id)
  }

  def findAll() = {
    log.debug("finding all schueler")

    implicit val reader = Schueler.BSONReader

    val query = BSONDocument()

    collection.find(query).toList pipeTo sender
  }

  def create(item: Schueler) = {
    log.debug("creating new Schueler '{}'", item)

    implicit val writer = Schueler.BSONWriter

    collection.insert(item) pipeTo sender
  }

  def load(id: String) = {
    log.debug("loading Schueler with id '{}'", id)
  }

  def update(item: Schueler) = {
    log.debug("updating Schueler with id '{}'", item)
  }

  def delete(id: String) = {
    log.debug("deleting Schueler with id '{}'", id)
  }
}