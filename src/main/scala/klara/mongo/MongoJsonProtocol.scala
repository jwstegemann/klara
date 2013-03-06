package klara.mongo

import spray.json.DefaultJsonProtocol
import spray.json._

import reactivemongo.bson.{BSONObjectID, BSONLong}

/*
 * Allows to serialize objects using special mongo-types to JSON and back
 */
class MongoJsonProtocol extends DefaultJsonProtocol {

  /*
   * provides conversion of BSONObjectID used by mongoDB into a valid JSON-String and back
   */
  implicit object BSONObjectIDFormat extends RootJsonFormat[BSONObjectID] {
    def write(id: BSONObjectID) =
      JsString(id.stringify)

    def read(value: JsValue) = value match {
      case JsString(id) =>
        new BSONObjectID(id)
      case _ => deserializationError("BSONObjectID expected")
    }
  }
  
  /*
   * provides conversion of BSONLong used by mongoDB into a valid JSON-Value and back
   */
  implicit object BSONLongFormat extends RootJsonFormat[BSONLong] {
    def write(longValue: BSONLong) =
      JsNumber(longValue.value)

    def read(value: JsValue) = value match {
      case JsNumber(bigDecimalValue) =>
        new BSONLong(bigDecimalValue.longValue)
      case _ => deserializationError("BSONLong expected")
    }
  }
}