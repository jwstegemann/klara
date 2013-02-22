package klara.mongo

import reactivemongo.bson._


abstract class Entity {
	def id: Option[BSONObjectID]
}