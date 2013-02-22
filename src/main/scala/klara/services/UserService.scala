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

import klara.auth._
import klara.auth.KlaraAuthJsonProtocol._

import scala.concurrent.duration._
import scala.concurrent._
import akka.util.Timeout

import language.postfixOps

import scala.compat.Platform
import java.util.UUID

import akka.pattern.ask
import akka.actor.ActorLogging

import spray.http.HttpHeaders._


// this trait defines our service behavior independently from the service actor
trait UserService extends HttpService with SprayJsonSupport with SessionAware { self : ActorLogging =>

  val userContextActor = actorRefFactory.actorFor("/user/userContext")

  private implicit val timeout = new Timeout(2 seconds)

  val userRoute = {
    pathPrefix("user") {
      path("login") {
        post {
          hostName { hostName =>
            entity(as[LoginRequest]) { loginRequest =>
              val future = (userContextActor ? CheckUserMsg(loginRequest.username, loginRequest.password))
              val result = future map {
                case Some(userContext : UserContext) => {
                  val sid = createSessionId(hostName)
                  sessionServiceActor ! CreateSessionMsg(sid, userContext, hostName)
                  val cookie = HttpCookie(SESSION_COOKIE_NAME, sid, path = Some("/"), maxAge = Some(3600))
                  HttpResponse(status=OK,headers=`Set-Cookie`(cookie) :: Nil)
                }
                case None => HttpResponse(status=Forbidden)
              }
              complete(result)
            }
          }
        }
      } ~
        get {
          authenticate(SessionCookieAuth()) { userContext =>
            path("info") {
              log.info("Userinfo for " + userContext.username + " requested")
              complete(userContext);
            }
          }
        }
    }
  }

  def createSessionId(hostName: String) = new UUID(Platform.currentTime, hostName.hashCode).toString

}
