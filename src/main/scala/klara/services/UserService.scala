package klara.services

import spray.routing.{ HttpService, RequestContext }
import spray.routing.directives.CachingDirectives
import spray.can.server.HttpServer
import spray.util._
import spray.http._
import MediaTypes._
import CachingDirectives._
import org.slf4j.{ Logger, LoggerFactory }
import spray.routing._
import spray.http._
import StatusCodes._
import Directives._

import spray.httpx.unmarshalling.pimpHttpEntity
import spray.json._
import spray.httpx.marshalling._
import spray.httpx.SprayJsonSupport

import klara.auth._
import klara.auth.KlaraAuthJsonProtocol._

import scala.concurrent.duration._
import scala.concurrent._
import akka.util.Timeout

import language.postfixOps

import scala.compat.Platform
import java.util.UUID

import klara.auth.AuthenticationConstants._

import akka.pattern.ask


// this trait defines our service behavior independently from the service actor
trait UserService extends HttpService with SprayJsonSupport {

  val sessionServiceActor = actorRefFactory.actorFor("/user/sessionService")
  val userContextActor = actorRefFactory.actorFor("/user/userContext")

  lazy val userLogger = LoggerFactory.getLogger(getClass);

  implicit val timeout = new Timeout(2 seconds)

  /*
  implicit val klaraRejectionHandler = RejectionHandler.fromPF {
	case AuthenticationRequiredRejection(scheme, realm, params) :: _ =>
		complete(Unauthorized, "Please login")
  }
  */

  val userRoute = {
    pathPrefix("user") {
      path("login") {
        post {
          hostName { hostName =>
            entity(as[LoginRequest]) { loginRequest =>
              val sid = createSessionId(hostName)
              //TODO: do not send cookie when rejecting!
              setCookie(HttpCookie(SESSION_COOKIE_NAME, sid, maxAge = Some(3600))) {
                val future = (userContextActor ? CheckUserMsg(loginRequest.username, loginRequest.password))

                val result = future map {
                  case Some(userContext : KlaraUserContext) => {
                    sessionServiceActor ! CreateSessionMsg(sid, userContext, hostName)
                    OK
                  }
                  case None => Forbidden
                }

                complete(result)
              }
            }
          }
        }
      } ~
        get {
          authenticate(SessionCookieAuth(sessionServiceActor)) { userContext =>
            path("info") {
              userLogger.info("Userinfo for " + userContext.username + " requested")
              complete(userContext);
            }
          }
        }
    }
  }

  def createSessionId(hostName: String) = new UUID(Platform.currentTime, hostName.hashCode).toString

}
