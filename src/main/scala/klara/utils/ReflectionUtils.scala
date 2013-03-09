package klara.utils

import klara.entity.Entity
import scala.reflect.runtime.universe._
import scala.language.postfixOps

object ReflectionUtils {

  def getFieldNames[T <: Product : TypeTag] : List[String] = {
    typeOf[T].declarations.collect {
      case acc: MethodSymbol if acc.isCaseAccessor => acc
    } map { acc => acc.name.decoded } toList
  }

}