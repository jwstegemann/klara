package klara

import akka.actor.{Props, Actor}
import spray.can.server.SprayCanHttpServerApp
import klara.services._

class RootServiceActor extends Actor with UserService with StaticService {

  def actorRefFactory = context

  def receive = runRoute(
    userRoute ~ staticRoute
  )
}

object Boot extends App with SprayCanHttpServerApp {

  // create and start our service actors
  val rootService = system.actorOf(Props[RootServiceActor], "root-service")

  // create a new HttpServer using our handler and tell it where to bind to
  newHttpServer(rootService) ! Bind(interface = "localhost", port = 8080)
}