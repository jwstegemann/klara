package klara.auth

import akka.actor._
import akka.pattern.pipe

import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.bson.handlers.BSONReader
import reactivemongo.bson.handlers.DefaultBSONHandlers.{DefaultBSONDocumentWriter,DefaultBSONReaderHandler}

import scala.concurrent.ExecutionContext.Implicits.global

import language.postfixOps


case class CheckUserMsg(username : String, password : String)

class UserContextActor extends Actor with ActorLogging {

  override def preStart =  {
    log.info("UserContextActor starting at: {}", self.path)
  }

  val config = context.system.settings.config

  val mongodbUrl = config.getString("klara.mongodb.url")
  val mongodbDb = config.getString("klara.mongodb.db")

  log.info("creating connection to {}@{}", mongodbDb, mongodbUrl)

  val connection = MongoConnection(List(mongodbUrl))
  val db = connection(mongodbDb)
  val collection = db("user")

  def receive = {
    case CheckUserMsg(username,password) => checkUser(username,password)
  }

  private val digest = java.security.MessageDigest.getInstance("SHA-256")

  private def sha(s: String): String = {
    val m = digest.digest(s.getBytes("UTF-8"));
    m map { c => (c & 0xff) toHexString } mkString
  }

  def checkUser(username: String, password: String) = {
    log.info("checking user {} with {}...", username, password)

    implicit val reader = KlaraUserContext.BSONReader

    val query = BSONDocument("username" -> BSONString(username), "password" -> BSONString(sha(password)))

    val userContextOption = collection.find(query).toList map {
      case userContext :: Nil => Some(userContext)
      case _ => None
    }

    userContextOption pipeTo sender
  }

}