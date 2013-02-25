package klara.entity

import spray.json.DefaultJsonProtocol
import spray.json._

import reactivemongo.bson._


abstract class Entity {
	def id: Option[BSONObjectID]
}

object EntityJsonProtocol extends DefaultJsonProtocol {

  implicit object klaraEntityFormat extends RootJsonFormat[Entity] {
   	def write(e: Entity) = JsString("undefined entity with id " + e.id.getOrElse("no id given"))
   	def read(value: JsValue) = deserializationError("pure Entity cannot be deserialized")
  }
}