package klara.entity

import klara.system.Message
import klara.system.Failable
import klara.system.ValidationException


trait Validation[T <: Entity] { self: Failable =>
	
	type Check = (T) => Either[Message, T]

	def validate(item: T, checks: List[Check]) = {
    
    val msgs = for {
      check <- checks
      msg <- check(item).left.toSeq
    } yield msg

    if (!msgs.isEmpty) failWith(ValidationException(msgs))
	}
}