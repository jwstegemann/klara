package klara.schueler

import akka.actor._
import akka.pattern.{pipe, ask}

import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.bson.handlers.BSONReader
import reactivemongo.bson.handlers.DefaultBSONHandlers.{DefaultBSONDocumentWriter,DefaultBSONReaderHandler}

import scala.concurrent.Future

import language.postfixOps

import klara.mongo.MongoUsingActor

import klara.system._
import klara.system.Severities._
import klara.schueler.Schueler.{BSONReader, BSONWriter}

import reactivemongo.core.commands.LastError

/*
 * available message-types for this actor
 */
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

  /*
   * find all items and send back the list to the sender
   */ 
  def findAll() = {
    log.debug("finding all schueler")
    collection.find(BSONDocument()).toList pipeTo sender
  }

  /*
   * create a new item
   */
  def create(item: Schueler) = {
    log.debug("creating new Schueler '{}'", item)

    if (!item.id.isEmpty) {
      Future.failed(ValidationException(Message("no id is allowed when creating an object", `ERROR`) :: Nil)) pipeTo sender
    }
    else {
      answerWithLastError(collection.insert(item), Inserted("not implemented yet"))
    }
  }

  /*
   * load and item
   */
  def load(id: String) = {
    log.debug("loading Schueler with id '{}'", id)
    val query = BSONDocument("_id" -> BSONObjectID(id))
    answerWithOptionNotFound(collection.find(query).headOption, id)
  }

  def update(item: Schueler) = {
    log.debug("updating Schueler with id '{}'", item)
    item.id match {
      case None => answerWithException(ValidationException(Message("id is required when updating an object", `ERROR`) :: Nil))
      case Some(id) => answerWithLastError(collection.update(BSONDocument("_id" -> item.id.get),item,defaultWriteConcern,false,false), Updated(1))  
    }
  }

  def delete(id: String) = {
    log.debug("deleting Schueler with id '{}'", id)
    val query = BSONDocument("_id" -> BSONObjectID(id))    
    answerWithLastError(collection.remove(query,defaultWriteConcern,true), Deleted(id))
  }
}