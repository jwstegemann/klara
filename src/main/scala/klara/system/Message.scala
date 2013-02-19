package klara.system

import spray.json.DefaultJsonProtocol

case class Message(text: String, details: String, severity : String, field: Option[String] = None)

case class Severity(severity: String) {
	override def toString() = severity
}

object Severities {
	val `DEBUG` = "DEBUG"
	val `INFO` = "INFO"
	val `WARN` = "WARN"
	val `ERROR` = "ERROR"
	val `FATAL` = "FATAL"
}

object MessageJsonProtocol extends DefaultJsonProtocol {
  implicit val severityFormat = jsonFormat1(Severity)
  implicit val messageFormat = jsonFormat4(Message.apply)
}