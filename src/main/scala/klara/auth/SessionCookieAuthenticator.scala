package klara.auth

import com.typesafe.config.Config
import spray.http._
import spray.util._
import HttpHeaders._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import spray.routing.authentication._
import spray.routing.{RequestContext,RoutingSettings,AuthenticationFailedRejection}

import scala.collection.concurrent.TrieMap
import java.util.UUID

import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.bson.handlers.DefaultBSONHandlers._

import org.slf4j.{Logger, LoggerFactory}




case class KlaraUserContext(username : String, info : String)

object AuthenticationService {

	val authLogger = LoggerFactory.getLogger(getClass);

	val SESSION_COOKIE_NAME = "klaraSessionId"

	case class Session(id : String, userContext : KlaraUserContext, host : String)

	val sessions = TrieMap[String,Session]()

	val digest = java.security.MessageDigest.getInstance("SHA-256")

	def sha( s:String ) : String = {
  		val m = digest.digest(s.getBytes("UTF-8"));
  		m map {c => (c & 0xff) toHexString} mkString
	}

	//TODO: get from config-DB
	val connection = MongoConnection( List( "localhost:27017" ) )
  	val db = connection("klara")
  	val collection = db("user")


	def checkUser(username : String, password : String) : Future[Option[KlaraUserContext]] = {
		val query = BSONDocument("username" -> BSONString(username), "password" -> BSONString(sha(password)))

  		collection.find(query).toList map { list =>
  			if (list.size == 1) {
  				Some(KlaraUserContext(username, list(0).getAs[BSONString]("lastName").get.value))
			}
			else {
				None
			}
  		}
	}

	def createSession(userContext : KlaraUserContext, hostName : String) : String = {
		val time : Long = 13215
		val host =  hostName.hashCode
		val sessionId = new UUID(time,host).toString
		sessions += (sessionId -> Session(sessionId, userContext, hostName))
		sessionId
	}

	def isSessionValid(sessionId : String, host : String) : Future[Option[KlaraUserContext]] = {
		//TODO: include get in future?
		authLogger.info("sessionId: " + sessionId)
		authLogger.info("host: " + host)
		//authLogger.info("username: " + sessions(sessionId).userContext.username)

		future {
			sessions.get(sessionId) match {
				case Some(Session(sessionId,userContext,host)) => Some(userContext)
				case None => None
			}
		}
	}
}

/**
 * A SessionCookieAuthenticator is a ContextAuthenticator that uses credentials passed to the server via the
 * HTTP `Authorization` header to authenticate the user and extract a user object.
 */
class SessionCookieAuthenticator(implicit val executionContext: ExecutionContext) extends ContextAuthenticator[KlaraUserContext] {

  def apply(ctx: RequestContext) = {
	val cookieOption : Option[HttpCookie] = ctx.request.cookies.find(_.name == AuthenticationService.SESSION_COOKIE_NAME) 
	
	cookieOption match {
		case Some(sessionCookie) => {
			AuthenticationService.isSessionValid(sessionCookie.content, ctx.request.host) map {
				case Some(userContext) => Right(userContext)
				case None => Left(AuthenticationFailedRejection("Klara"))
			}
		}
		case None => future { Left(AuthenticationFailedRejection("Klara")) }
	}
  }

}


object SessionCookieAuth {
  def apply() (implicit ec: ExecutionContext): SessionCookieAuthenticator =
    new SessionCookieAuthenticator()
}