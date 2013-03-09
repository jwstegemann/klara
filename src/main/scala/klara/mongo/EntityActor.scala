package klara.mongo

import akka.actor._
import akka.pattern.pipe

import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.bson.handlers.DefaultBSONHandlers.{DefaultBSONDocumentWriter,DefaultBSONReaderHandler}
import reactivemongo.bson.handlers.{BSONReader,BSONWriter}

import scala.concurrent.Future
import scala.reflect.ClassTag

import language.postfixOps

import klara.system._
import klara.system.Severities._

import klara.entity._

abstract class EntityActor[T <: Entity: ClassTag](val collectionName: String)
  (implicit val bsonReader: BSONReader[T], val bsonWriter: BSONWriter[T]) extends MongoUsingActor with LastErrorMapping {

  val collection = db(collectionName)

  def receive = {
    case FindAll() => findAll()
    case Create(item: T) => create(item)
    case Load(id) => load(id)
    case Update(item: T) => update(item)
    case Delete(id) => delete(id)
  }

  override def preStart =  {
    log.info(s"EntityActor for collection $collectionName started at: {}", self.path)
  }

  /*
   * find all items and send back the list to the sender
   */ 
  def findAll() = {
    log.debug(s"finding all entites in $collectionName")
    collection.find(BSONDocument()).toList pipeTo sender
  }

  /*
   * create a new item
   */
  def create(item: T) = {
    log.debug(s"creating new entity in $collectionName '{}'", item)

    if (!item._id.isEmpty) {
      failWith(ValidationException(Message("no id is allowed when creating an object", `ERROR`) :: Nil))
    }
    else {
      item._id.generate
      item.version.update
      (collection.insert(item).recoverWithInternalServerError.mapToInserted(item._id.toString)) pipeTo sender
    }
  }

  /*
   * load an item
   */
  def load(id: String) = {
    log.debug(s"loading item from $collectionName with id '{}'", id)
    val query = BSONDocument("_id" -> BSONObjectID(id))
    failIfEmpty(collection.find(query).headOption, id)
  }

  /*
   * update an item
   */
  def update(item: T) = {
    log.debug(s"updating entity in $collectionName with id '{}'", item)
    
    if (item._id.isEmpty || item.version.isEmpty) {
      failWith(ValidationException(Message("id and version are required when updating an object", `ERROR`) :: Nil))
    }
    else {
      val query = BSONDocument("_id" -> item._id.toBSON, "version" -> item.version.toBSON)
      item.version.update
      (collection.update(query, item, defaultWriteConcern,false,false).recoverWithInternalServerError.mapToUpdated) pipeTo sender
    }
  }

  /*
   * delete an item
   */
  def delete(id: String) = {
    log.debug(s"deleting entity from $collectionName with id '{}'", id)
    val query = BSONDocument("_id" -> BSONObjectID(id))    
    (collection.remove(query,defaultWriteConcern,true).recoverWithInternalServerError.mapToDeleted(id)) pipeTo sender
  }
	
}