package klara.entity

import klara.system.Message

package object validation {
  type Result[T] = Either[Message, T]
  type Check[T] = (T) => Result[T]
}
