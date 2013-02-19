package klara.schueler

import reactivemongo.bson._
import reactivemongo.bson.handlers.{BSONReader,BSONWriter}

import klara.mongo.MongoJsonProtocol


case class Schueler(
  id: Option[BSONObjectID],
  name: String, 
  vorname: String
)

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

object SchuelerJsonProtocol extends MongoJsonProtocol {

  implicit val schuelerFormat = jsonFormat3(Schueler.apply)
}