package klara.entity.validation

import klara.system.Message
import klara.system.Severities._
import klara.system.Failable
import klara.system.ValidationException

import klara.entity._


trait Validation[T <: Entity] { self: Failable =>

	def validate(item: T, checks: List[Check[T]]) = {
    
    val msgs = for {
      check <- checks
      msg <- check(item).left.toSeq
    } yield msg

    if (!msgs.isEmpty) {
      failWith(ValidationException(msgs))
      false
    }
    else {
      true
    }
	}

	/*
   * checks if no id is present
   */
  def hasNoId(item: T): Result[T] = {
    if (item._id.isEmpty) Right(item)
    else Left(Message("no id is allowed when creating an object", `ERROR`))
  }

  /*
   * checks if no id is present
   */
  def hasId(item: T): Result[T] = {
    if (!item._id.isEmpty) Right(item)
    else Left(Message("id required when updating an object", `ERROR`))
  }

  /*
   * checks if no id is present
   */
  def hasVersion(item: T): Result[T] = {
    if (!item.version.isEmpty) Right(item)
    else Left(Message("version required when updating an object", `ERROR`))
  }
}

trait EntityValidator[T] {
  //TODO: add parameter for entity state
  def checks() : List[Check[T]]
}

