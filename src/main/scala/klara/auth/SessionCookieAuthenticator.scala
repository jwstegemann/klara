package klara.auth

import com.typesafe.config.Config
import spray.http._
import spray.util._
import HttpHeaders._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import spray.routing.authentication._
import spray.routing.{RequestContext,RoutingSettings,AuthenticationFailedRejection}

import klara.auth._

import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.bson.handlers.BSONReader
import reactivemongo.bson.handlers.DefaultBSONHandlers.{DefaultBSONDocumentWriter,DefaultBSONReaderHandler}

import org.slf4j.{Logger, LoggerFactory}

import language.postfixOps
import spray.json._

import akka.pattern.ask
import akka.actor._
import akka.util.Timeout
import scala.concurrent.duration._


object AuthenticationService {

  val SESSION_COOKIE_NAME = "sid"

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
}

/**
 * A SessionCookieAuthenticator is a ContextAuthenticator that uses credentials passed to the server via the
 * HTTP `Authorization` header to authenticate the user and extract a user object.
 */
class SessionCookieAuthenticator(sessionServiceActor : ActorRef)(implicit val executionContext: ExecutionContext) extends ContextAuthenticator[KlaraUserContext] {

  implicit val timeout = new Timeout(2 seconds)

  def apply(ctx: RequestContext) = {

    //TODO: find cookie in future
    val cookieOption: Option[HttpCookie] = ctx.request.cookies.find(_.name == AuthenticationService.SESSION_COOKIE_NAME)

    cookieOption match {
      case Some(sessionCookie) => {
        sessionServiceActor ? IsSessionValidMsg(sessionCookie.content, ctx.request.host) map {
          case Some(userContext : KlaraUserContext) => Right(userContext)
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