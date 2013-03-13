package klara.mongo.bson

import reactivemongo.bson._
import scala.reflect.runtime.universe._
import klara.entity._

import klara.utils.ReflectionUtils

import klara.system._


trait ProductConverters extends StandardConverters {

  implicit object MongoIdConverter extends BSONConverter[MongoId] {
    def toBSON(element: MongoId) = element.id
    def fromBSON(value: BSONValue) = value match {
      case id : BSONObjectID => new MongoId(id)
      case x => throw BSONDeserializationError("Expected BSONObjectID to deserialize to id of Product but found " + x.getClass)
    }
  }

  implicit object MongoVersionConverter extends BSONConverter[MongoVersion] {
    def toBSON(element: MongoVersion) = BSONLong(element.version)
    def fromBSON(value: BSONValue) = value match {
      case version : BSONLong => new MongoVersion(version.value)
      case x => throw BSONDeserializationError("Expected BSONLong to deserialize to version of Product but found " + x.getClass)
    }
  }

  implicit class KlaraAppendableBSONDocument(doc: AppendableBSONDocument) {
    def appendValue[T](label: String, value: T)(implicit converter: BSONConverter[T]) = {
      converter.toBSON(value) match {
        case BSONNull => doc
        case convertedValue => doc.append(label -> convertedValue)
      }
    }
  }

  implicit class KlaraTraversableBSONDocument(doc: TraversableBSONDocument) {
    def readValue[T](label: String)(implicit converter: BSONConverter[T]) : T = {
      converter.fromBSON(doc.get(label).get)
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
        val doc : KlaraAppendableBSONDocument = BSONDocument().toAppendable
        doc.appendValue(a, element.productElement(0).asInstanceOf[A])
          .appendValue(b, element.productElement(1).asInstanceOf[B])
          .appendValue(c, element.productElement(2).asInstanceOf[C])
          .appendValue(d, element.productElement(3).asInstanceOf[D])
      }

      def fromBSON(value : BSONValue) = value match {
        case document : BSONDocument => {
          val doc :  KlaraTraversableBSONDocument = document.toTraversable
          construct(
            doc.readValue[A](a),
            doc.readValue[B](b),
            doc.readValue[C](c),
            doc.readValue[D](d)
          )
        }
        case x =>  throw BSONDeserializationError("Expected BSONDocument to deserialize to Product but found " + x.getClass)
      }
    }
}