package klara.auth

import spray.http._
import spray.util._
import HttpHeaders._
import spray.routing.authentication._
import spray.routing.{RequestContext,RoutingSettings,AuthenticationFailedRejection}
import spray.json._

import scala.concurrent._
import scala.concurrent.duration._

import klara.auth._

import akka.pattern.ask
import akka.actor._
import akka.util.Timeout


import language.postfixOps


/**
 * A SessionCookieAuthenticator is a ContextAuthenticator that uses credentials passed to the server via the
 * HTTP `Authorization` header to authenticate the user and extract a user object.
 */
class SessionCookieAuthenticator(sessionServiceActor : ActorRef)(implicit val executionContext: ExecutionContext) extends ContextAuthenticator[UserContext] {

  implicit val timeout = new Timeout(2 seconds)

  def apply(ctx: RequestContext) = {

    //TODO: find cookie in future
    val cookieOption: Option[HttpCookie] = ctx.request.cookies.find(_.name == SESSION_COOKIE_NAME)

    cookieOption match {
      case Some(sessionCookie) => {
        sessionServiceActor ? IsSessionValidMsg(sessionCookie.content, ctx.request.host) map {
          case Some(userContext : UserContext) => Right(userContext)
          case None => Left(AuthenticationFailedRejection("Klara"))
        }
      }
      case None => future { Left(AuthenticationFailedRejection("Klara")) }
    }
  }
}

object SessionCookieAuth {
  def apply(sessionServiceActor : ActorRef)(implicit ec : ExecutionContext): SessionCookieAuthenticator =
    new SessionCookieAuthenticator(sessionServiceActor)
}