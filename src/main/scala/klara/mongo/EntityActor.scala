package klara.mongo

import akka.actor._
import akka.pattern.pipe

import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.bson.handlers.DefaultBSONHandlers.{DefaultBSONDocumentWriter,DefaultBSONReaderHandler}
import reactivemongo.bson.handlers.{BSONReader,BSONWriter}

import scala.concurrent.Future

import language.postfixOps

import klara.system._
import klara.system.Severities._


abstract class EntityActor[T <: Entity](val entityName: String, val collectionName: String, 
	val bsonReader: BSONReader[T], val bsonWriter: BSONWriter[T]) extends MongoUsingActor {

  val collection = db(collectionName)

  implicit val bsonReaderImplicit = bsonReader
  implicit val bsonWriterImplicit = bsonWriter

  override def preStart =  {
    log.info(s"$entityName actor started at: {}", self.path)
  }

  /*
   * find all items and send back the list to the sender
   */ 
  def findAll() = {
    log.debug(s"finding all $entityName")
    collection.find(BSONDocument()).toList pipeTo sender
  }

  /*
   * create a new item
   */
  def create(item: T) = {
    log.debug(s"creating new $entityName '{}'", item)

    if (!item.id.isEmpty) {
      Future.failed(ValidationException(Message("no id is allowed when creating an object", `ERROR`) :: Nil)) pipeTo sender
    }
    else {
      answerWithLastError(collection.insert(item), Inserted("not implemented yet"))
    }
  }

  /*
   * load an item
   */
  def load(id: String) = {
    log.debug(s"loading $entityName with id '{}'", id)
    val query = BSONDocument("_id" -> BSONObjectID(id))
    answerWithOptionNotFound(collection.find(query).headOption, id)
  }

  /*
   * update an item
   */
  def update(item: T) = {
    log.debug(s"updating $entityName with id '{}'", item)
    item.id match {
      case None => answerWithException(ValidationException(Message("id is required when updating an object", `ERROR`) :: Nil))
      case Some(id) => answerWithLastError(collection.update(BSONDocument("_id" -> item.id.get),item,defaultWriteConcern,false,false), Updated(1))  
    }
  }

  /*
   * delete an item
   */
  def delete(id: String) = {
    log.debug(s"deleting $entityName with id '{}'", id)
    val query = BSONDocument("_id" -> BSONObjectID(id))    
    answerWithLastError(collection.remove(query,defaultWriteConcern,true), Deleted(id))
  }
	
}