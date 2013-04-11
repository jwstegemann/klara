package klara.services

import spray.routing.{ HttpService, RequestContext }
import spray.routing.directives.CachingDirectives
import spray.can.server.HttpServer
import spray.util._
import spray.http._
import MediaTypes._
import CachingDirectives._
import spray.routing._
import spray.http._
import StatusCodes._
import Directives._

import spray.httpx.unmarshalling.pimpHttpEntity
import spray.json._
import spray.httpx.marshalling._
import spray.httpx.SprayJsonSupport

import scala.concurrent.duration._
import scala.concurrent._
import akka.util.Timeout

import language.postfixOps

import akka.pattern.ask
import akka.actor.ActorLogging

import spray.http.HttpHeaders._

import klara.system._

//import klara.system.DictionaryJsonProtocol._

/*object Schulform extends KeyDictionary {
   val gymnasium = Value("gymnasium", "Gymnasium", "Gymnasium")
   val realschule = Value("realschule", "Realschule", "Realschule")
   val hauptschule = Value("hauptschule", "Hauptschule", "Hauptschule")
   val grundschule = Value("grundschule", "Grundschule", "Grundschule")
}*/

// this trait defines our service behavior independently from the service actor
trait DictionaryService extends HttpService with SprayJsonSupport with SessionAware { self : ActorLogging =>

//  val userContextActor = actorRefFactory.actorFor("//userContext")

  private implicit val timeout = new Timeout(2 seconds)

  val dictionaryRoute = {
    pathPrefix("dict") {
//          log.info("getting dictionary for " + dictionary)
          complete(Schulform.Grundschule)
    }
  }
}
