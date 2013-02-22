package klara.system

case class InternalServerErrorException(val messages: List[Message]) extends Exception

case class NotFoundException(val message: Message) extends Exception

case class ValidationException(val messages: List[Message]) extends Exception