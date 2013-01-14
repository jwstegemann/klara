package klara.services

import spray.routing.{HttpService, RequestContext}
import spray.routing.directives.CachingDirectives
import spray.can.server.HttpServer
import spray.util._
import spray.http._
import MediaTypes._
import CachingDirectives._
import org.slf4j.{Logger, LoggerFactory}

import spray.routing.authentication.BasicAuth
import karla.auth._


// this trait defines our service behavior independently from the service actor
trait UserService extends HttpService {

  val userLogger = LoggerFactory.getLogger(getClass);

  val userRoute = {
    authenticate(BasicAuth(MongoAuthenticator.fromMongoDB, "myStats")) { user =>
      pathPrefix("user") {
        get {
          path("login") {
          	respondWithMediaType(`application/json`) {
                userLogger.info("User " + user.username + " logged in")
                //TODO: return user object here
                complete("{\"username\": \"Karl2\", \"info\": \"Otto2\"}");
            }
          } ~
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
