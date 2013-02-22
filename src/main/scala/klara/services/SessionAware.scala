package klara.services

import spray.routing.HttpService

trait SessionAware { self: HttpService =>

	protected implicit val sessionServiceActor = actorRefFactory.actorFor("/user/sessionService")

}