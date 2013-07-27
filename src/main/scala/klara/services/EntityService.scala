package klara.services

import spray.http._
import spray.routing._
import spray.can.server.HttpServer
import spray.util._
import MediaTypes._
import StatusCodes._
import Directives._

import spray.httpx.unmarshalling.pimpHttpEntity
import spray.json._
import spray.httpx.marshalling._
import spray.httpx.SprayJsonSupport

import spray.httpx.unmarshalling.Unmarshaller
import spray.httpx.marshalling.Marshaller

import scala.concurrent.duration._
import scala.concurrent._
import akka.util.Timeout

import language.postfixOps

import akka.pattern.ask
import akka.actor.ActorLogging

import klara.auth.{SessionCookieAuth, UserContext}
import klara.system._

import klara.mongo.EntityActor
import akka.actor.ActorRef

import klara.entity._
import scala.reflect._

import klara.entity.EntityJsonProtocol._


trait EntityService {

  private implicit val timeout = new Timeout(5 seconds)

  def route[T <: Entity: ClassTag](prefix: String, entityActor: ActorRef, userContext: UserContext)(implicit marshaller: spray.httpx.marshalling.Marshaller[Future[T]],
  		listMarshaller: Marshaller[Future[List[T]]],
  		insMarsh: Marshaller[scala.concurrent.Future[Inserted]],
  		delMarsh: Marshaller[scala.concurrent.Future[Deleted]],
  		updMarsh: Marshaller[scala.concurrent.Future[Updated]],
  		unmarshaller: Unmarshaller[T]) = {

    pathPrefix(prefix) {
			path("") {
			  post {
			    entity(as[T]) { item =>
			      complete((entityActor ? Create(item)).mapTo[Inserted])
			    }
			  } ~
			  get {
			    //TODO: is this necessary or is it enough to be called just once per change
			    dynamic {
			      complete((entityActor ? FindAll()).mapTo[List[T]])
			    }
			  }
			} ~ 
			path(Rest) { id: String =>
			  get {
					dynamic {
				  complete((entityActor ? Load(id)).mapTo[T])
				}
				  } ~
			  delete {
			    dynamic {
			      complete((entityActor ? Delete(id)).mapTo[Deleted])
			    }
			  } ~
			  put {
			    entity(as[T]) { item =>
			      complete((entityActor ? Update(item)).mapTo[Updated])
			    }
			  }
			}
    }
  }
}