package klara.mongo

import spray.json.DefaultJsonProtocol
import spray.json._

import reactivemongo.bson.BSONObjectID


class MongoJsonProtocol extends DefaultJsonProtocol {

  implicit object BSONObjectIDFormat extends RootJsonFormat[BSONObjectID] {
    def write(id: BSONObjectID) =
      JsString(id.stringify)

    def read(value: JsValue) = value match {
      case JsString(id) =>
        new BSONObjectID(id)
      case _ => deserializationError("BSONObjectID expected")
    }
  }
  
}