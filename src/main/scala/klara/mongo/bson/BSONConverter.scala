package klara.mongo.bson

import reactivemongo.bson._

abstract class BSONConverter[T] {
  def toBSON(element: T): BSONValue
  //abstract def fromBSON(value: BSONValue): T
}
