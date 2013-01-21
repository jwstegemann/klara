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
import scala.concurrent.Await

import language.postfixOps

import scala.compat.Platform
import java.util.UUID


// this trait defines our service behavior independently from the service actor
trait UserService extends HttpService with SprayJsonSupport {

  implicit val sessionServiceActor = actorRefFactory.actorFor("/user/session-service")

  val userLogger = LoggerFactory.getLogger(getClass);

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
              val userContextOption: Option[KlaraUserContext] = Await.result(AuthenticationService.checkUser(loginRequest.username, loginRequest.password), 1 second)

              userContextOption match {
                case Some(userContext) => {
                  val sid = createSessionId(hostName)
                  setCookie(HttpCookie(AuthenticationService.SESSION_COOKIE_NAME, sid, maxAge = Some(3600))) {
                    sessionServiceActor ! CreateSessionMsg(sid, userContext, hostName)
                    userLogger.info("User " + loginRequest.username + ";" + loginRequest.password + " logged in.")
                    complete(OK);
                  }
                }
                case None => reject(AuthorizationFailedRejection)
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

  def createSessionId(hostName: String): String = {
    val time = Platform.currentTime
    val host = hostName.hashCode
    new UUID(time, host).toString
  }
}
