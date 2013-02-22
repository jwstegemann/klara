package klara.services

import spray.http._
import StatusCodes._
import MediaTypes._
import spray.json._
import spray.httpx.SprayJsonSupport
import spray.routing._
import spray.routing.Directives._

import akka.actor.ActorLogging

import klara.system.MessageJsonProtocol._
import klara.system._
import klara.system.Severities._


trait MessageHandling { self: SprayJsonSupport with ActorLogging =>
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
}