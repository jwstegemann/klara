package klara.services

import scala.concurrent.duration._
import scala.util.{ Success, Failure }
import akka.pattern.ask
import spray.routing.{ HttpService, RequestContext }
import spray.routing.directives.CachingDirectives
import spray.can.server.HttpServer
import spray.util._
import spray.http._
import MediaTypes._
import CachingDirectives._
import org.slf4j.{ Logger, LoggerFactory }

// this trait defines our service behavior independently from the service actor
trait StaticService extends HttpService {

  val staticLogger = LoggerFactory.getLogger(getClass);

  //TODO: configure cache
  lazy val staticCache = routeCache(maxCapacity = 200, timeToIdle = Duration("12 h"))

  val staticRoute = {
    pathPrefix("static") {
      cache(staticCache) {
        getFromResourceDirectory("static")
      }
    }
  }
}
