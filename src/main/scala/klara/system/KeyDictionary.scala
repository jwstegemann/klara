package klara.system

import spray.json._
import reactivemongo.bson._

import scala.collection.mutable.{Map, LinkedHashMap}

import klara.mongo.bson.BSONConverter


case class KeyValue(key: String, shortText: String, longText: String)

object DictionaryJsonProtocol extends DefaultJsonProtocol {
  implicit val keyValueFormat = jsonFormat3(KeyValue.apply)
}

abstract class Dictionary() extends DefaultJsonProtocol {

	/*
	 * JSON-format for dictionarie-values
	 */
	implicit object valueJSONFormat extends RootJsonFormat[Value] {
    def write(value: Value) = JsString(value.key)
    def read(value: JsValue) = value match {
      case key : JsString => valueMap(key.value)
      case _ => deserializationError("wrong format for Dictionary.Value")
    }
  }

	/*
	 * BSON-format for dictionarie-values
	 */
  implicit object valueBSONFormat extends BSONConverter[Value] {
    def toBSON(value: Value) = BSONString(value.key)
    def fromBSON(value: BSONValue) = value match {
      case key : BSONString => valueMap.get(key.value) match {
      	case Some(result) => result
      	case None => throw BSONDeserializationError(s"No value for key '$key.value' defined")
      }
      case x => throw BSONDeserializationError("Expected BSONString to deserialize to dictionary-value but found " + x.getClass)
    }
  }

  // map to store possible values
	private val valueMap = LinkedHashMap[String,Value]()

	def create(key: String, shortText: String, longText: String) : Value = {
		val value = Value(key, shortText, longText)
		valueMap += (key -> value)
		value
	}

	def values = valueMap.values map { value => KeyValue(value.key, value.shortText, value.longText) }

	case class Value(key: String, shortText: String, longText: String)
}
