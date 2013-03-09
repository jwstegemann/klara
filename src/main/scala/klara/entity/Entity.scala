package klara.entity

import spray.json.DefaultJsonProtocol
import spray.json._

import reactivemongo.bson._


class MongoId(var id : BSONObjectID = null) {
	def toBSON = id

	def generate = {
		id = BSONObjectID.generate
	}

	override def toString = id match {
		case null => "no id available" 
		case x => x.stringify
	}

	def isEmpty = (id == null)
}


class MongoVersion(var version: Long = 0) {
	def toBSON = BSONLong(version)

	def update = {
		version = System.currentTimeMillis
	}

	override def toString = version.toString

	def isEmpty = (version == 0)
}

abstract class Entity {
	def _id: MongoId
	def version: MongoVersion
}

object Entity {

}

object EntityJsonProtocol extends DefaultJsonProtocol {

  implicit object klaraEntityFormat extends RootJsonFormat[Entity] {
   	def write(e: Entity) = JsString("abstract entity with id " + e.toString)
   	def read(value: JsValue) = deserializationError("pure Entity cannot be deserialized")
  }
}