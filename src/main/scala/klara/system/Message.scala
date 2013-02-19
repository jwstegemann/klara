package klara.system

case class Result(ok: Boolean, messages: List[Message])

case class Message(text: String, details: String, severity : Severity, field: String = null)

case class Severity(severity: String) {
	override def toString() = severity
}

object Severities {
	val `DEBUG` = Severity("DEBUG")
	val `INFO` = Severity("INFO")
	val `WARN` = Severity("WARN")
	val `ERROR` = Severity("ERROR")
	val `FATAL` = Severity("FATAL")
}
