package klara.schueler

import reactivemongo.bson.BSONArray
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONString
import reactivemongo.bson.handlers.BSONReader
import spray.json.DefaultJsonProtocol

case class Schueler(name: String, vorname: String)

object Schueler {
  implicit object BSONReader extends BSONReader[Schueler] {
    def fromBSON(document: BSONDocument): Schueler = {
      val doc = document.toTraversable

      Schueler(
        doc.getAs[BSONString]("name").get.value,
        doc.getAs[BSONString]("vorname").get.value
      )
    }
  }
}

object SchuelerJsonProtocol extends DefaultJsonProtocol {
  implicit val schuelerFormat = jsonFormat2(Schueler.apply)
}