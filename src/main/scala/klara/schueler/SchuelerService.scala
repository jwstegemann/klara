package klara.schueler

import spray.http._
import spray.routing._
import spray.can.server.HttpServer
import spray.util._
import MediaTypes._
import StatusCodes._
import Directives._

import spray.httpx.unmarshalling.pimpHttpEntity
import spray.json._
import spray.httpx.marshalling._
import spray.httpx.SprayJsonSupport

import scala.concurrent.duration._
import scala.concurrent._
import akka.util.Timeout

import language.postfixOps

import akka.pattern.ask
import akka.actor.ActorLogging

import klara.schueler.SchuelerJsonProtocol._
import klara.auth.{SessionCookieAuth, UserContext}
import klara.system._
import klara.services.{MessageHandling, SessionAware}

import klara.services.EntityService
import klara.entity._



// this trait defines our service behavior independently from the service actor
trait SchuelerService extends HttpService with SprayJsonSupport with MessageHandling { self : ActorLogging =>

  val schuelerActor = actorRefFactory.actorFor("/user/schueler")

  val entityService = new EntityService[Schueler]("schueler", schuelerActor)

  def schuelerRoute(userContext: UserContext) = entityService.route(userContext)
}
