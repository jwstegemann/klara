package klara.services

import spray.http._
import StatusCodes._
import MediaTypes._
import spray.json._
import spray.httpx.SprayJsonSupport
import spray.routing.Directives._

import klara.system.MessageJsonProtocol._
import klara.system._
import klara.system.Severities._

import scala.concurrent.{Future, ExecutionContext}
import akka.actor.Actor

//import scala.language.implicitConversions


trait MessageHandling { self: SprayJsonSupport =>

}