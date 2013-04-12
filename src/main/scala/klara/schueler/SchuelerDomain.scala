package klara.schueler

import reactivemongo.bson._
import reactivemongo.bson.handlers.{BSONReader,BSONWriter}

import klara.entity._
import klara.mongo.MongoJsonProtocol
import klara.mongo.bson.{BSONProtocol, ProductConverters}

import klara.system.{MessageFormats, Dictionary}

object Schulform extends Dictionary("Schulform") {
  type Schulform = Value
  val Grundschule = create("grundschule", "Grundschule", "Grundschule")
  val Hauptschule = create("hauptschule", "Hauptschule", "Hauptschule")
  val Realschule = create("realschule", "Realschule", "Realschule")
  val Gymnasium = create("gymnasium", "Gymnasium", "Gymnasium")
}

object Geschlecht extends Dictionary("Geschlecht") {
  type Geschlecht = Value
  val maennlich = create("m","männlich","männlich")
  val weiblich = create("f","weiblich","weiblich")
}

import Geschlecht._

case class Schueler (
  _id: MongoId,
  name: String, 
  vorname: String,
  geschlecht: Geschlecht,
  version: MongoVersion
) extends Entity

object Schueler extends BSONProtocol[Schueler] with ProductConverters {

  override def entityConverter = productConverter5(Schueler.apply)

}

object SchuelerJsonProtocol extends MongoJsonProtocol with MessageFormats {

  implicit val schuelerFormat = jsonFormat5(Schueler.apply)
}