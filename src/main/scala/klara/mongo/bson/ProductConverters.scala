package klara.mongo.bson

import reactivemongo.bson._
import scala.reflect.runtime.universe._
import klara.entity._

import klara.utils.ReflectionUtils


trait ProductConverters extends StandardConverters {

  implicit object MongoIdConverter extends BSONConverter[MongoId] {
    def toBSON(element: MongoId) = element.toBSON
  }

  implicit object MongoVersionConverter extends BSONConverter[MongoVersion] {
    def toBSON(element: MongoVersion) = element.toBSON
  }

  implicit class KlaraBSONDocument(doc: AppendableBSONDocument) {
    def appendValue[T](label: String, value: T)(implicit converter: BSONConverter[T]) = {
      converter.toBSON(value) match {
        case BSONNull => doc
        case convertedValue => doc.append(label -> convertedValue)
      }
    }
  }

  def productConverter4[A :BSONConverter,B :BSONConverter,C :BSONConverter,D :BSONConverter, T <: Product : TypeTag](construct: (A, B, C, D) => T): BSONConverter[T] = {
      val fieldNames = ReflectionUtils.getFieldNames[T]
      productConverter4WithNames(construct,
        fieldNames(0),
        fieldNames(1),
        fieldNames(2),
        fieldNames(3))
  }

  def productConverter4WithNames[A :BSONConverter,B :BSONConverter,C :BSONConverter,D :BSONConverter, T <: Product](construct: (A, B, C, D) => T,
      a: String, b: String, c: String, d: String): BSONConverter[T] =
    new BSONConverter[T] {
      def toBSON(element: T) = {
        val doc : KlaraBSONDocument = BSONDocument().toAppendable
        doc.appendValue(a, element.productElement(0).asInstanceOf[A])
          .appendValue(b, element.productElement(1).asInstanceOf[B])
          .appendValue(c, element.productElement(2).asInstanceOf[C])
          .appendValue(d, element.productElement(3).asInstanceOf[D])
      }
    }
}