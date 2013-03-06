package klara.entity

import spray.json.DefaultJsonProtocol
import spray.json._

import reactivemongo.bson._


abstract class Entity {
	def id: Option[BSONObjectID]
	def version: Option[BSONLong]
}

object EntityJsonProtocol extends DefaultJsonProtocol {

  implicit object klaraEntityFormat extends RootJsonFormat[Entity] {
   	def write(e: Entity) = JsString("abstract entity with id " + e.id.getOrElse("no id given"))
   	def read(value: JsValue) = deserializationError("pure Entity cannot be deserialized")
  }
}