package klara.mongo

import spray.json.DefaultJsonProtocol
import spray.json._
import klara.entity._

import reactivemongo.bson.{BSONObjectID, BSONLong}

/*
 * Allows to serialize objects using special mongo-types to JSON and back
 */
class MongoJsonProtocol extends DefaultJsonProtocol {

  implicit object MongoIdFormat extends RootJsonFormat[MongoId] {
    def write(e: MongoId) = JsString(e.toString)
    def read(value: JsValue) = value match {
      case JsNull => new MongoId()
      case id : JsString => new MongoId(BSONObjectID(id.value))
      case _ => deserializationError("wrong format for MongoId")
    }
  }

  implicit object MongoVersionFormat extends RootJsonFormat[MongoVersion] {
    def write(e: MongoVersion) = JsString(e.version.toString)
    def read(value: JsValue) = value match {
      case JsNull => new MongoVersion()
      case id : JsString => new MongoVersion(id.value.toLong)
      case _ => deserializationError("wrong format for MongoVersion")
    }
  }


  //TODO: Following is not needed anymore?
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