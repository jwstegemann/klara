package klara.schueler

import reactivemongo.bson._
import reactivemongo.bson.handlers.{BSONReader,BSONWriter}

import klara.entity._
import klara.entity.validation._
import klara.mongo.MongoJsonProtocol
import klara.mongo.bson.{BSONProtocol, ProductConverters}

import klara.system.{MessageFormats, Dictionary}
import klara.system.Message
import klara.system.Severities._


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


object Schueler extends BSONProtocol[Schueler] with ProductConverters with MongoJsonProtocol with MessageFormats with EntityValidator[Schueler] {

  // BSON-Serialization
  override def entityConverter = productConverter5(Schueler.apply)

  // JSON-Serialization
  implicit val schuelerFormat = jsonFormat5(Schueler.apply)

  // Validation
  override def checks() = checkName _ :: Nil

  def checkName(schueler: Schueler): Result[Schueler] = {
    if (schueler.name.startsWith("Ca")) Right(schueler)
    else Left(Message("Namde muss mit Ca beginnen!",`ERROR`, field=Some("name")))
  }

}
