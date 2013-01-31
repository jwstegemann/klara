package klara.mongo

import akka.actor._
import reactivemongo.api._

trait MongoUsingActor extends Actor with ActorLogging {

  implicit val ec = context.dispatcher

  val config = context.system.settings.config

  private val mongodbUrl = config.getString("klara.mongodb.url")
  private val mongodbDb = config.getString("klara.mongodb.db")

  protected val connection = MongoConnection(List(mongodbUrl))
  protected val db = connection(mongodbDb)

  log.info("creating connection to {}@{}", mongodbDb, mongodbUrl)

}