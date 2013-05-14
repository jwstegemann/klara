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

  implicit object IntConverter extends BSONConverter[Int] {
    def toBSON(element: Int) = BSONInteger(element)
    def fromBSON(value: BSONValue) = value match {
      case intValue : BSONInteger => intValue.value
      case x => throw BSONDeserializationError("Expected BSONInteger to deserialize to Int but found " + x.getClass)
    }
  }

  implicit object LongConverter extends BSONConverter[Long] {
    def toBSON(element: Long) = BSONLong(element)
    def fromBSON(value: BSONValue) = value match {
      case longValue : BSONLong => longValue.value
      case x => throw BSONDeserializationError("Expected BSONLong to deserialize to Long but found " + x.getClass)
    }
  }

  implicit object DoubleConverter extends BSONConverter[Double] {
    def toBSON(element: Double) = BSONDouble(element)
    def fromBSON(value: BSONValue) = value match {
      case doubleValue : BSONDouble => doubleValue.value
      case x => throw BSONDeserializationError("Expected BSONDouble to deserialize to Double but found " + x.getClass)
    }
  }

  implicit object BooleanConverter extends BSONConverter[Boolean] {
    def toBSON(element: Boolean) = BSONBoolean(element)
    def fromBSON(value: BSONValue) = value match {
      case booleanValue : BSONBoolean => booleanValue.value
      case x => throw BSONDeserializationError("Expected BSONBoolean to deserialize to Boolean but found " + x.getClass)
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