package klara.mongo.bson

import reactivemongo.bson._
import klara.system.BSONDeserializationError
import scala.reflect.ClassTag


trait StandardConverters {
  type BC[T] = BSONConverter[T]

  implicit object StringConverter extends BSONConverter[String] {
    def toBSON(element: String) = BSONString(element)
    def fromBSON(value: BSONValue) = value match {
      case stringValue : BSONString => stringValue.value
      case x => throw BSONDeserializationError("Expected BSONString to deserialize to String but found " + x.getClass)
    }
  }

  implicit def BSONValueConverter[T <: BSONValue : ClassTag] = new BSONValueConverter[T]

  class BSONValueConverter[T <: BSONValue : ClassTag] extends BSONConverter[T] {
    def toBSON(element: T) = element
    def fromBSON(value: BSONValue) = value match {
      case bsonValue : T => bsonValue
      case x => throw BSONDeserializationError("Error converting BSONValue of type " + x.getClass)
    }
  }

  implicit def OptionConverter[T : BSONConverter] = new OptionConverter[T]

  //FIXME: change BSONNull to "not relevant"
  class OptionConverter[T : BSONConverter](implicit innerConverter: BSONConverter[T]) extends BSONConverter[Option[T]] {
    def toBSON(element: Option[T]) = {
      element match {
        case Some(item) => innerConverter.toBSON(item)
        case None => BSONUndefined
      }
    }

    def fromBSON(value: BSONValue) = {
      value match {
        case BSONUndefined => None
        case someValue: BSONValue => Some(innerConverter.fromBSON(someValue))
      }
    }
  }

}