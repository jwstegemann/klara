package klara.schule

import reactivemongo.bson._
import reactivemongo.bson.handlers.{BSONReader,BSONWriter}

import klara.entity._
import klara.entity.validation._
import klara.mongo.MongoJsonProtocol
import klara.mongo.bson.{BSONProtocol, ProductConverters}

import klara.system.MessageFormats
import klara.system.Message
import klara.system.Severities._

import klara.dictionary.Schulform._


case class Schule (
  _id: MongoId,
  name: String, 
  schulform: Schulform,
  version: MongoVersion
) extends Entity


object Schule extends BSONProtocol[Schule] with ProductConverters with MongoJsonProtocol with MessageFormats with EntityValidator[Schule] {

  // BSON-Serialization
  override def entityConverter = productConverter4(Schule.apply)

  // JSON-Serialization
  implicit val schuleFormat = jsonFormat4(Schule.apply)

  // Validation
  override def checks() = Nil
}
