package klara.system

import akka.actor._
import akka.pattern.pipe

import scala.concurrent.duration._
import scala.concurrent.Future
import scala.collection.concurrent.TrieMap

import scala.concurrent.duration._

import scala.collection.mutable.{Map, HashMap}

import klara.system.Severities._

/*
 * Messages for DictionaryServiceActor
 */
case class GetValues(dictionary : String)


object DictionaryRegistry {

  val dictionaries = new HashMap[String, Dictionary]

  //FIXME: throw fatal exception if dictionary is already present!
  def register(name: String, dictionary : Dictionary) = {
    dictionaries += (name -> dictionary)
  }

  def size = dictionaries.size
}

/*
 * DictionaryServicActor
 */
class DictionaryServiceActor extends Actor with ActorLogging with Failable {


  override def preStart =  {
    log.info("DictionaryServiceActor started at: {} with {} dictionaries registered", self.path, DictionaryRegistry.size)
  }

  def receive = {
    case GetValues(dictionary) => getValues(dictionary)
  }

  def getValues(dictionary: String) = {
    //log.info("Dict: {}", dictionary)
    DictionaryRegistry.dictionaries.get(dictionary) match {
      case Some(dict) => sender ! dict.values
      case None => failWith(NotFoundException(Message(s"no dictionary named '$dictionary' registered", `ERROR`) :: Nil))
    }
  }

  
}