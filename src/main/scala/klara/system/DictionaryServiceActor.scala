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

case class Register(name: String, dictionary : Dictionary)


/*
 * DictionaryServicActor
 */
class DictionaryServiceActor extends Actor with ActorLogging with Failable {

  val dictionaries = new HashMap[String, Dictionary]

  override def preStart =  {
    log.info("DictionaryServiceActor started at: {}", self.path)

    import klara.schueler.Schulform

    register("Schulform", Schulform)
  }

  def receive = {
    case GetValues(dictionary) => getValues(dictionary)
    case Register(name, dictionary) => register(name, dictionary)
  }

  def getValues(dictionary: String) = {
    log.info("Dict: {}", dictionary)
    dictionaries.get(dictionary) match {
      case Some(dict) => sender ! dict.values
      case None => failWith(NotFoundException(Message(s"no dictionary named '$dictionary' registered", `ERROR`) :: Nil))
    }
  }

  def register(name: String, dictionary : Dictionary) = {
    dictionaries += (name -> dictionary)
  }
}