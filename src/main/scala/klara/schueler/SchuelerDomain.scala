package klara.schueler

import reactivemongo.bson._
import reactivemongo.bson.handlers.{BSONReader,BSONWriter}

import klara.entity._
import klara.entity.validation._
import klara.mongo.MongoJsonProtocol
import klara.mongo.bson.{BSONProtocol, ProductConverters}

import klara.system.MessageFormats
import klara.system.Message
import klara.system.Severities._

import klara.dictionary.Geschlecht._


case class Vertragspartner (
  name: String,
  vorname: String,
  strasse: String,
  hausnummer: String,
  plz: String,
  ort: String
)

case class Schueler (
  _id: MongoId,
  name: String, 
  vorname: String,
  geschlecht: Geschlecht,
  vertragspartner: Vertragspartner,
  version: MongoVersion
) extends Entity


object Schueler extends BSONProtocol[Schueler] with ProductConverters with MongoJsonProtocol with MessageFormats with EntityValidator[Schueler] {

  // BSON-Serialization
  implicit def vertragspartnerConverter = productConverter6(Vertragspartner.apply)
  override def entityConverter = productConverter6(Schueler.apply)

  // JSON-Serialization
  implicit val vertragspartnerFormat = jsonFormat6(Vertragspartner.apply)
  implicit val schuelerFormat = jsonFormat6(Schueler.apply)

  // Validation
  override def checks() = checkName _ :: Nil

  def checkName(schueler: Schueler): Result[Schueler] = {
    if (schueler.name.startsWith("Ca")) Right(schueler)
    else Left(Message("Namde muss mit Ca beginnen!",`ERROR`, field=Some("name")))
  }

}
