package klara.mongo.bson

import reactivemongo.bson._
import klara.system.BSONDeserializationError
import scala.reflect.ClassTag


trait CollectionConverters {

/*  implicit def ListConverter[T : BSONConverter] = new ListConverter[T]

  class ListConverter[T : BSONConverter](implicit innerConverter: BSONConverter[T]) extends BSONConverter[List[T]] {
    def toBSON(element: List[T]) = {

    }

    def fromBSON(value: BSONValue) = {
      value match {
        case BSONUndefined => new List
        case someValue: BSONValue => Some(innerConverter.fromBSON(someValue))
      }
    }
  }
*/
}