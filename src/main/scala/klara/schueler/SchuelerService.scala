package klara.schueler

import spray.routing._

import spray.json._
import spray.httpx.marshalling._
import spray.httpx.SprayJsonSupport

import akka.actor.ActorLogging

import klara.schueler.Schueler._
import klara.auth.UserContext
import klara.system._
import klara.services.MessageHandling

import klara.services.EntityService
import klara.entity._



// this trait defines our service behavior independently from the service actor
trait SchuelerService extends HttpService with SprayJsonSupport with MessageHandling with EntityService {self : ActorLogging =>

  def schuelerRoute(userContext: UserContext) = route[Schueler]("schueler", actorRefFactory.actorFor("/user/schueler"), userContext)

}
