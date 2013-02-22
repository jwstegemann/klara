package klara.mongo

import scala.concurrent.Future
import akka.actor._
import akka.pattern.pipe

import reactivemongo.api._
import reactivemongo.core.commands.LastError
import reactivemongo.bson.BSONDocument
import reactivemongo.core.commands.GetLastError

import klara.system._
import klara.system.Severities._


trait MongoUsingActor extends Actor with ActorLogging {

  // provide ExecutionContext
  implicit val ec = context.dispatcher
  
  // get DB-settings from config-file
  val config = context.system.settings.config

  private val mongodbUrl = config.getString("klara.mongodb.url")
  private val mongodbDb = config.getString("klara.mongodb.db")

  // establish connecttion to mongoDB
  protected val connection = MongoConnection(List(mongodbUrl))
  protected val db = connection(mongodbDb)

  log.info("creating connection to {}@{}", mongodbDb, mongodbUrl)

  //TODO: is this correct and efficient
  val defaultWriteConcern = GetLastError(true,None,false)

  /*
   * converts a future instance of LastError returned by most mongo-operations into a list of messages
   */
  def answerWithLastError[T](lastError: Future[LastError], successValue: T) = {
    (lastError map {
      case LastError(true, _, _, _, _) => successValue
    } recover {
      case LastError(_, err, code, msg, doc) => {
        log.error("mongoDB-Error: {}{}{}{}",err.getOrElse(""),
          code match {
            case Some(s : Int) => s" with code '$s'"
            case None => ""
          },
          msg match {
            case Some(s) => s" with message '$s'"
            case None => ""
          },
          doc match {
            case Some(doc) => {
              val pretty = BSONDocument.pretty(doc)
              s" in document '$pretty'"
            }
            case None => ""
          }
        )
        throw InternalServerErrorException(
          Message("A database error occured. Please inform your system-administrator.", `ERROR`, err.getOrElse("no details available")) :: Nil
        )
      }
    }) pipeTo sender 
  }

  def answerWithOptionNotFound[T](item: Future[Option[T]], id: String) = {
    (item map {
      case Some(t) => t
      case None => throw NotFoundException(Message(s"id '$id' could not be found",`ERROR`))
    }) pipeTo sender
  }
}