package klara.utils

case class HandlingError(id: Int, message: String, details: String)

import spray.json._
import klara.system.MessageJsonProtocol._
import klara.system._
import klara.system.Severities._

object MessageHelper {
	def serializeList(list: List[Message]) = list.toJson.compactPrint
}