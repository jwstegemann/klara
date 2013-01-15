package klara.services

import spray.routing.{HttpService, RequestContext}
import spray.routing.directives.CachingDirectives
import spray.can.server.HttpServer
import spray.util._
import spray.http._
import MediaTypes._
import CachingDirectives._
import org.slf4j.{Logger, LoggerFactory}
import spray.routing._
import spray.http._
import StatusCodes._
import Directives._

import spray.httpx.unmarshalling.pimpHttpEntity
import spray.json._
import spray.httpx.marshalling._
import spray.httpx.SprayJsonSupport


import spray.routing.authentication.BasicAuth
import klara.auth._
import klara.auth.KlaraAuthJsonProtocol._

import scala.concurrent.duration._
import scala.concurrent.Await


// this trait defines our service behavior independently from the service actor
trait UserService extends HttpService with SprayJsonSupport{

  val userLogger = LoggerFactory.getLogger(getClass);

  //TODO: make a trait from this
  /*
  implicit val klaraRejectionHandler = RejectionHandler.fromPF {
	case AuthenticationRequiredRejection(scheme, realm, params) :: _ =>
		complete(Unauthorized, "Please login")
  }
  */
  /*
											*/
  val userRoute = {
    pathPrefix("user") {
          path("login") {
			post {
				hostName { hostName =>
					entity(as[LoginRequest]) { loginRequest =>
						val userContextOption : Option[KlaraUserContext] = Await.result(AuthenticationService.checkUser(loginRequest.username, loginRequest.password)
								, 1 second)

						userContextOption match {
							case Some(userContext) => { 
								setCookie(HttpCookie(AuthenticationService.SESSION_COOKIE_NAME, 
										AuthenticationService.createSession(userContext,hostName), maxAge=Some(3600))) {
											userLogger.info("User " + loginRequest.username + ";" + loginRequest.password + " logged in.")
											complete(OK);
								 }
							}	
							case None => reject(AuthenticationFailedRejection("Klara"))
						}
					}
				}
			}
          } ~
		  get {
			  authenticate(SessionCookieAuth()) { user =>
				  path("info") {
					respondWithMediaType(`application/json`) {
						userLogger.info("Userinfo for " + user.username + " requested")
						//TODO: return user object here
						complete("{\"username\": \"Karl\", \"info\": \"Otto\"}");
					}
				  }
			  }
		  }
        }
    }
}
