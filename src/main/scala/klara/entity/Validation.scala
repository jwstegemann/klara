package klara.entity

import klara.system.Message
import klara.system.Severities._
import klara.system.Failable
import klara.system.ValidationException


trait Validation[T <: Entity] { self: Failable =>
	
	type Result = Either[Message, T]
	type Check = (T) => Result

	def defaultChecks : List[Check] = Nil

	def validate(item: T, checks: List[Check]) = {
    
    val msgs = for {
      check <- checks
      msg <- check(item).left.toSeq
    } yield msg

    if (!msgs.isEmpty) failWith(ValidationException(msgs))
	}

	/*
   * checks if no id is present
   */
  def hasNoId(item: T): Result = {
    if (item._id.isEmpty) Right(item)
    else Left(Message("no id is allowed when creating an object", `ERROR`))
  }

  /*
   * checks if no id is present
   */
  def hasId(item: T): Result = {
    if (!item._id.isEmpty) Right(item)
    else Left(Message("id required when updating an object", `ERROR`))
  }

  /*
   * checks if no id is present
   */
  def hasVersion(item: T): Result = {
    if (!item.version.isEmpty) Right(item)
    else Left(Message("version required when updating an object", `ERROR`))
  }
}