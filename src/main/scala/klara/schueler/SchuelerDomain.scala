package klara.schueler

import reactivemongo.bson._
import reactivemongo.bson.handlers.{BSONReader,BSONWriter}

import klara.entity.Entity
import klara.mongo.MongoJsonProtocol

import klara.system.MessageFormats


case class Schueler (
  id: Option[BSONObjectID],
  name: String, 
  vorname: String,
  version: Option[BSONLong]
) extends Entity

/** TODO - deal with non-case products by giving them _1 _2 etc. */
class CaseClassReflector(root: Product) {
}

abstract class BSONConverter[T] {
  def toBSON(element: T): BSONValue
  //abstract def fromBSON(value: BSONValue): T
}

trait StandardConverters {
  implicit object StringConverter extends BSONConverter[String] {
    def toBSON(element: String) = BSONString(element)
  }

  implicit object OptionConverter extends BSONConverter[Option[Any]] {
    def toBSON(element: Option[Any]) = BSONString(element.toString)
  }  
}

class EntityWriter[T <: Entity] extends StandardConverters{

  def writeElement[T](doc: AppendableBSONDocument, name: String, element: T)(implicit converter: BSONConverter[T]) = {
  }

  //TODO: cache List of writers instead of iterating here each time for performance reasons
  def write(entity : T) : BSONDocument = {
    val doc = BSONDocument().toAppendable
    doc
  }

}

object Schueler {

  implicit object BSONReader extends BSONReader[Schueler] {
    def fromBSON(document: BSONDocument): Schueler = {
      val doc = document.toTraversable

      Schueler(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[BSONString]("name").get.value,
        doc.getAs[BSONString]("vorname").get.value,
        doc.getAs[BSONLong]("version")
      )
    }
  }

  implicit object BSONWriter extends BSONWriter[Schueler] {
    def toBSON(schueler: Schueler) = {

      //Macros.log(schueler)
      Macros.printFields[Schueler]


//      val writer = new EntityWriter[Schueler]()
//      writer.write(schueler)

      BSONDocument(
        "_id" -> schueler.id.getOrElse(BSONObjectID.generate),
        "name" -> BSONString(schueler.name),
        "vorname" -> BSONString(schueler.vorname),
        "version" -> BSONLong(System.currentTimeMillis)
      )
    }
  }

}

object SchuelerJsonProtocol extends MongoJsonProtocol with MessageFormats {

  implicit val schuelerFormat = jsonFormat4(Schueler.apply)
}