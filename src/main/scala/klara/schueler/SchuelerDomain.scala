package klara.schueler

import reactivemongo.bson._
import reactivemongo.bson.handlers.{BSONReader,BSONWriter}

import klara.entity.Entity
import klara.mongo.MongoJsonProtocol

import klara.system.MessageFormats

import reactivemongo.bson.Implicits._


case class Schueler (
  id: Option[BSONObjectID],
  name: String, 
  vorname: String,
  version: Option[BSONLong]
) extends Entity



abstract class BSONConverter[T] {
  def toBSON(element: T): BSONValue
  //abstract def fromBSON(value: BSONValue): T
}

trait StandardConverters {
  implicit object StringConverter extends BSONConverter[String] {
    def toBSON(element: String) = BSONString(element)
  }

  implicit def BSONValueConverter[T <: BSONValue] = new BSONValueConverter[T]

  class BSONValueConverter[T <: BSONValue] extends BSONConverter[T] {
    def toBSON(element: T) = element
  }

  implicit def OptionConverter[T : BSONConverter] = new OptionConverter[T]

  class OptionConverter[T : BSONConverter](implicit innerConverter: BSONConverter[T]) extends BSONConverter[Option[T]] {
    def toBSON(element: Option[T]) = {
      element match {
        case Some(item) => innerConverter.toBSON(item)
        case None => BSONNull
      }
    }
  }

}

trait ProductConverters {

  implicit class KlaraBSONDocument(doc: AppendableBSONDocument) {
    def appendValue[T](label: String, value: T)(implicit converter: BSONConverter[T]) = {
      converter.toBSON(value) match {
        case BSONNull => doc
        case convertedValue => doc.append(label -> convertedValue)
      }
    }
  }

  def productConverter4[A :BSONConverter,B :BSONConverter,C :BSONConverter,D :BSONConverter, T <: Product](construct: (A, B, C, D) => T,
      a: String, b: String, c: String, d: String): BSONConverter[T] =
    new BSONConverter[T] {
      def toBSON(element: T) = {
        //FIXME: This has to become much nicer!
        val doc : KlaraBSONDocument = BSONDocument().toAppendable
        doc.appendValue(a, element.productElement(0).asInstanceOf[A])
          .appendValue(b, element.productElement(1).asInstanceOf[B])
          .appendValue(c, element.productElement(2).asInstanceOf[C])
          .appendValue(d, element.productElement(3).asInstanceOf[D])
      }
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

  implicit object BSONWriter extends BSONWriter[Schueler] with StandardConverters with ProductConverters{
    def toBSON(schueler: Schueler) = {
      //Macros.printFields[Schueler]
      implicit def schuelerConverter = productConverter4(Schueler.apply,"_id","name","vorname","version")

//      val mySchueler = schueler.copy(id=Some(schueler.id.getOrElse(BSONObjectID.generate)), version=Some(BSONLong(System.currentTimeMillis)))

      val doc: BSONDocument = schuelerConverter.toBSON(schueler).asInstanceOf[BSONDocument]
      printf("MyDoc: " + BSONDocument.pretty(doc))

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