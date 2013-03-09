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

  implicit object BSONReader extends BSONReader[Schueler] {
    def fromBSON(document: BSONDocument): Schueler = {
      val doc = document.toTraversable

      Schueler(
        new MongoId(doc.getAs[BSONObjectID]("_id").get),
        doc.getAs[BSONString]("name").get.value,
        doc.getAs[BSONString]("vorname").get.value,
        new MongoVersion(doc.getAs[BSONLong]("version").get.value)
      )
    }
  }
}

object SchuelerJsonProtocol extends MongoJsonProtocol with MessageFormats {

  implicit val schuelerFormat = jsonFormat4(Schueler.apply)
}