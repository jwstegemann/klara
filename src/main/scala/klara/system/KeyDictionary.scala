package klara.system

import spray.json._

import scala.collection.mutable.{Map, LinkedHashMap}


case class KeyValue(key: String, shortText: String, longText: String)

object DictionaryJsonProtocol extends DefaultJsonProtocol {
  implicit val keyValueFormat = jsonFormat3(KeyValue.apply)
}

abstract class Dictionary() extends DefaultJsonProtocol {

	implicit object valueFormat extends RootJsonFormat[Value] {
    def write(value: Value) = JsString(value.key)
    def read(value: JsValue) = value match {
      case key : JsString => valueMap(key.value)
      case _ => deserializationError("wrong format for Dictionary.Value")
    }
  }

	private def valueMap = new LinkedHashMap[String,Value]()

	def create(key: String, shortText: String, longText: String) : Value = {
		val value = Value(key, shortText, longText)
		valueMap += (key -> value)
		value
	}

	def values = valueMap mapValues { value => KeyValue(value.key, value.shortText, value.longText)}

	case class Value(key: String, shortText: String, longText: String)
}

object Schulform extends Dictionary {
	type Schulform = Value
	val Grundschule = create("grundschule", "Grundschule", "Grundschule")
}

object Zahlweise extends Dictionary {
	type Zahlweise = Value
	val monatlich = create("monatlich", "monatlich", "monatlich")
}

import Schulform._
import Zahlweise._


object Test {
	def testMe = {
		val sf: Schulform = Grundschule
		val zw: Zahlweise = monatlich
	}
}