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
import klara.system.Implicits._


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

  //TODO: is this correct and efficient?
  val defaultWriteConcern = GetLastError(true,None,false)

  /*
   * fails future with a NotFound-exception when option is empty
   */
  def failIfEmpty[T](item: Future[Option[T]], id: String) = {
    (item map {
      case Some(t) => t
      case None => throw NotFoundException(Message(s"id '$id' could not be found",`ERROR`))
    }) pipeTo sender
  }

  /*
   * replies with excetion as answer to sender
   */
  def failWith(ex: Exception) = {
    Future.failed(ex) pipeTo sender
  }

}