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
import reactivemongo.bson.handlers.BSONReader
import reactivemongo.bson.handlers.DefaultBSONHandlers.{DefaultBSONDocumentWriter,DefaultBSONReaderHandler}

import org.slf4j.{Logger, LoggerFactory}

import language.postfixOps
import spray.json._

import scala.compat.Platform


//TODO: exclude as actor!
object AuthenticationService {

  private val authLogger = LoggerFactory.getLogger(getClass);

  val SESSION_COOKIE_NAME = "sid"

  private case class Session(id: String, userContext: KlaraUserContext, host: String)

  //TODO: schedule to remove old sessions when in actor
  private val sessions = TrieMap[String, Session]()

  private val digest = java.security.MessageDigest.getInstance("SHA-256")

  private def sha(s: String): String = {
    val m = digest.digest(s.getBytes("UTF-8"));
    m map { c => (c & 0xff) toHexString } mkString
  }

  //TODO: get from config-DB
  val connection = MongoConnection(List("localhost:27017"))
  val db = connection("klara")
  val collection = db("user")

  def checkUser(username: String, password: String): Future[Option[KlaraUserContext]] = {
    implicit val reader = KlaraUserContext.BSONReader

    val query = BSONDocument("username" -> BSONString(username), "password" -> BSONString(sha(password)))

    collection.find(query).toList map {
      case userContext :: Nil => Some(userContext)
      case _ => None
    }
  }

  def createSession(userContext: KlaraUserContext, hostName: String): String = {
    val time = Platform.currentTime
    val host = hostName.hashCode
    val sessionId = new UUID(time, host).toString
    sessions += (sessionId -> Session(sessionId, userContext, hostName))
    sessionId
  }

  def isSessionValid(sessionId: String, host: String): Future[Option[KlaraUserContext]] = {
    future {
      sessions.get(sessionId) match {
        //TODO: implement session-timeout
        case Some(Session(sessionId, userContext, host)) => Some(userContext)
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
    val cookieOption: Option[HttpCookie] = ctx.request.cookies.find(_.name == AuthenticationService.SESSION_COOKIE_NAME)

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
  def apply()(implicit ec: ExecutionContext): SessionCookieAuthenticator =
    new SessionCookieAuthenticator()
}