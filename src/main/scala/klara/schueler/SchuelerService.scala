package klara.schueler

//import spray.http._
import spray.routing._
//import spray.util._
//import MediaTypes._
//import StatusCodes._
//import Directives._

import spray.json._
import spray.httpx.marshalling._
import spray.httpx.SprayJsonSupport

import akka.actor.ActorLogging

// import klara.schueler.SchuelerJsonProtocol._
import klara.schueler.Schueler._
import klara.auth.UserContext
import klara.system._
import klara.services.MessageHandling

import klara.services.EntityService
import klara.entity._



// this trait defines our service behavior independently from the service actor
trait SchuelerService extends HttpService with SprayJsonSupport with MessageHandling with EntityService {self : ActorLogging =>

  override val entityActor = actorRefFactory.actorFor("/user/schueler")
  override val prefix = "schueler"

  def schuelerRoute(userContext: UserContext) = route(userContext)

}
