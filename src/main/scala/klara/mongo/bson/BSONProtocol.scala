package klara.mongo.bson

import reactivemongo.bson._
import reactivemongo.bson.handlers.{BSONReader,BSONWriter}

import klara.entity.Entity


trait BSONProtocol[T <: Entity] {

	//FIXME: is this possible as implicit?
	def entityConverter: BSONConverter[T]

	implicit object BSONWriter extends BSONWriter[T] {
    def toBSON(entity: T) = {
      entityConverter.toBSON(entity).asInstanceOf[BSONDocument]
  	}
  }

}