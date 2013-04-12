package klara

import akka.actor.{Props, Actor, ActorLogging}
import spray.can.server.SprayCanHttpServerApp
import klara.services._

import klara.auth.{SessionCookieAuth, UserContext}

import klara.auth.SessionServiceActor
import klara.auth.UserContextActor
import klara.system.DictionaryServiceActor

import klara.schueler.SchuelerActor
import klara.schueler.SchuelerService


class RootServiceActor extends Actor with ActorLogging with SchuelerService with UserService with StaticService with DictionaryService with SessionAware {

  def actorRefFactory = context

  def receive = runRoute(
    userRoute ~ 
    staticRoute ~ 
    authenticate(SessionCookieAuth()(sessionServiceActor, context.dispatcher)) { userContext => //FIXME: make sessionServiceActor implicit again
      schuelerRoute(userContext) ~
      dictionaryRoute
    }
  )
}

object Boot extends App with SprayCanHttpServerApp {
  
  // create and start the session service
  val sessionService = system.actorOf(Props[SessionServiceActor], "sessionService")	
  // create and start the userContext service
  val userContext = system.actorOf(Props[UserContextActor], "userContext")  
  // create and start the dictironary service
  val dictionaryService = system.actorOf(Props[DictionaryServiceActor], "dictionaryService")  

  /*
   * Domain Actors 
   */  
  // create and start the schueler service
  val schueler = system.actorOf(Props[SchuelerActor], "schueler")  

  /*
   * Web Actors
   */
  // create and start our routing service actors
  val rootService = system.actorOf(Props[RootServiceActor], "root-service")

  // create a new HttpServer using our handler and tell it where to bind to
  newHttpServer(rootService) ! Bind(interface = "localhost", port = 8080)
}