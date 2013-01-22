package klara.auth

import akka.actor._
import scala.collection.concurrent.TrieMap

case class IsSessionValidMsg(sessionId : String, host : String)

case class CreateSessionMsg(sid : String, userContext : KlaraUserContext, hostName : String)


class SessionServiceActor extends Actor with ActorLogging {

  override def preStart =  {
    log.info("SessionServiceActor starting at: {}", self.path)
  }

  case class Session(id: String, userContext: KlaraUserContext, host: String)

  //TODO: schedule to remove old sessions when in actor
  private val sessions = TrieMap[String, Session]()

  def receive = {
    case IsSessionValidMsg(sessionId, host) => isSessionValid(sessionId, host)
    case CreateSessionMsg(sid, userContext, hostName) => createSession(sid, userContext, hostName)
  }

  def isSessionValid(sessionId: String, host: String) = {
      //TODO: use map instead of match?
      sessions.get(sessionId) match {
        //TODO: implement session-timeout
        case Some(Session(sessionId, userContext, host)) => sender ! Some(userContext)
        case None => sender ! None
      }
  }

  def createSession(sid : String, userContext : KlaraUserContext, hostName : String) = {
    sessions += (sid -> Session(sid, userContext, hostName))
    log.info("created session '{}' for user '{}'", sid, userContext.username)
  }  
}