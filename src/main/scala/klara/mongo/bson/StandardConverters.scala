package klara.mongo.bson

import reactivemongo.bson._


trait StandardConverters {
  type BC[T] = BSONConverter[T]

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