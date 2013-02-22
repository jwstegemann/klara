package klara.schueler

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

import scala.concurrent.duration._
import scala.concurrent._
import akka.util.Timeout

import language.postfixOps

import akka.pattern.ask
import akka.actor.ActorLogging

import klara.schueler.SchuelerJsonProtocol._
import klara.auth.SessionCookieAuth
import klara.system._
import klara.services.{MessageHandling, SessionAware}




// this trait defines our service behavior independently from the service actor
trait SchuelerService extends HttpService with SprayJsonSupport with MessageHandling with SessionAware { self : ActorLogging =>

  val schuelerActor = actorRefFactory.actorFor("/user/schueler")

  private implicit val timeout = new Timeout(5 seconds)

  val schuelerRoute = {
    pathPrefix("schueler") {
      authenticate(SessionCookieAuth()) { userContext =>
        path("") {
          post {
            entity(as[Schueler]) { schueler =>
              complete((schuelerActor ? Create(schueler)).mapTo[Inserted])
            }
          } ~
          get {
            //TODO: is this necessary or is it enough to be called just once per change
            dynamic {
              complete((schuelerActor ? FindAll()).mapTo[List[Schueler]])
            }
          }
        } ~ 
        path(Rest) { id: String =>
          get {
            dynamic {
              complete((schuelerActor ? Load(id)).mapTo[Schueler])
            }
          } ~
          delete {
            dynamic {
              complete((schuelerActor ? Delete(id)).mapTo[Deleted])
            }
          } ~
          put {
            entity(as[Schueler]) { schueler =>
              complete((schuelerActor ? Update(schueler)).mapTo[Updated])
            }
          }
        }
      }
    }
  }

}
