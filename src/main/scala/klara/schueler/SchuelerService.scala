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

import klara.system.Message
import klara.system.Severities._

import klara.services.MessageHandling

import klara.system._


// this trait defines our service behavior independently from the service actor
trait SchuelerService extends HttpService with SprayJsonSupport with MessageHandling { self : ActorLogging =>

  val schuelerActor = actorRefFactory.actorFor("/user/schueler")

  private implicit val timeout = new Timeout(5 seconds)

  //TODO: make this implicit in a trait?
  private val sessionServiceActor = actorRefFactory.actorFor("/user/sessionService")

  /*
  implicit val klaraRejectionHandler = RejectionHandler.fromPF {
	case AuthenticationRequiredRejection(scheme, realm, params) :: _ =>
		complete(Unauthorized, "Please login")
  }
  */

  implicit val klaraExceptionHandler = ExceptionHandler.fromPF {
    case InternalServerErrorException(messages) => complete(InternalServerError, messages)
    case NotFoundException(message) => complete(NotFound, message)
    case ValidationException(messages) => complete(PreconditionFailed, messages)
    case t: Throwable => {
      log.error(t, s"Unexpected error:")
      complete(InternalServerError, Message("An unexpected Error occured. Please inform your system administrator.", `ERROR`))
    }
  }

  val schuelerRoute = {
    pathPrefix("schueler") {
      authenticate(SessionCookieAuth(sessionServiceActor)) { userContext =>
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
