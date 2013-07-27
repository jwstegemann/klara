package klara.schule

import spray.routing._

import spray.json._
import spray.httpx.marshalling._
import spray.httpx.SprayJsonSupport

import akka.actor.ActorLogging

import klara.schule.Schule._
import klara.auth.UserContext
import klara.system._
import klara.services.MessageHandling

import klara.services.EntityService
import klara.entity._



// this trait defines our service behavior independently from the service actor
trait SchuleService extends HttpService with SprayJsonSupport with MessageHandling with EntityService {self : ActorLogging =>

  def schuleRoute(userContext: UserContext) = route[Schule]("schule", actorRefFactory.actorFor("/user/schule"), userContext)

}
