package klara.schueler

import reactivemongo.bson._
import reactivemongo.bson.handlers.{BSONReader,BSONWriter}

import klara.entity.Entity
import klara.mongo.MongoJsonProtocol

import klara.system.WithMessages

case class Schueler(
  id: Option[BSONObjectID],
  name: String, 
  vorname: String
) extends Entity


object Schueler {

  implicit object BSONReader extends BSONReader[Schueler] {
    def fromBSON(document: BSONDocument): Schueler = {
      val doc = document.toTraversable

      Schueler(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[BSONString]("name").get.value,
        doc.getAs[BSONString]("vorname").get.value
      )
    }
  }

  implicit object BSONWriter extends BSONWriter[Schueler] {
    def toBSON(schueler: Schueler) = {
      BSONDocument(
        "_id" -> schueler.id.getOrElse(BSONObjectID.generate),
        "name" -> BSONString(schueler.name),
        "vorname" -> BSONString(schueler.vorname)
      )
    }
  }

}

object SchuelerJsonProtocol extends MongoJsonProtocol with WithMessages {

  implicit val schuelerFormat = jsonFormat3(Schueler.apply)
}