package klara.schueler

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

import klara.schueler.SchuelerJsonProtocol._

import klara.auth.SessionCookieAuth


// this trait defines our service behavior independently from the service actor
trait SchuelerService extends HttpService with SprayJsonSupport { self : ActorLogging =>

  val schuelerActor = actorRefFactory.actorFor("/user/schueler")

  private implicit val timeout = new Timeout(2 seconds)

  //TODO: make this implicit in a trait?
  private val sessionServiceActor = actorRefFactory.actorFor("/user/sessionService")

  /*
  implicit val klaraRejectionHandler = RejectionHandler.fromPF {
	case AuthenticationRequiredRejection(scheme, realm, params) :: _ =>
		complete(Unauthorized, "Please login")
  }
  */

  val schuelerRoute = {
    pathPrefix("schueler") {
      authenticate(SessionCookieAuth(sessionServiceActor)) { userContext =>
        path("") {
          get {
            val list = (schuelerActor ? FindAll()).mapTo[List[Schueler]]
            complete(list)
          } ~
          post {
            entity(as[Schueler]) { schueler =>
              schuelerActor ? Create(schueler)
              complete(OK)
            }
          }
        } 
      }
    }
  }

}
