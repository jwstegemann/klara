package klara.schueler

import reactivemongo.bson._
import reactivemongo.bson.handlers.{BSONReader,BSONWriter}

import klara.entity._
import klara.mongo.MongoJsonProtocol
import klara.mongo.bson.{BSONProtocol, ProductConverters}

import klara.system.MessageFormats


case class Schueler (
  _id: MongoId,
  name: String, 
  vorname: String,
  version: MongoVersion
) extends Entity

object Schueler extends BSONProtocol[Schueler] with ProductConverters {

  override def entityConverter = productConverter4(Schueler.apply)

}

object SchuelerJsonProtocol extends MongoJsonProtocol with MessageFormats {

  implicit val schuelerFormat = jsonFormat4(Schueler.apply)
}