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
import klara.entity._
import klara.entity.validation._

abstract class EntityActor[T <: Entity: ClassTag](val collectionName: String, validator: EntityValidator[T])
  (implicit val bsonReader: BSONReader[T], val bsonWriter: BSONWriter[T]) extends MongoUsingActor with LastErrorMapping with Validation[T] {

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

    if (validate(item, hasNoId _ :: validator.checks())) {
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
    
    if (validate(item, hasId _ :: hasVersion _ :: validator.checks())) {
      val query = BSONDocument("_id" -> item._id.id, "version" -> item.version.asBSON)
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