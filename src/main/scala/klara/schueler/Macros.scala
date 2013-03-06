package klara.schueler

import klara.entity.Entity

//import scala.language.experimental.macros

import scala.reflect.runtime.universe._

object Macros extends ReflectionUtils {
//  import scala.language.experimental.macros
//  import scala.reflect.macros.Context

  //def log[D <: Entity](domain: D): Unit = println("hallo")

  def printFields[T <: Entity : TypeTag] = {

    val caseType = typeOf[T]
    val accessors = caseType.declarations.collect {
      case acc: MethodSymbol if acc.isCaseAccessor => acc
    }.toList

    for (acc <-accessors) {
      println("name: " + acc.name.decoded)
      println("type:" + acc.typeSignatureIn(caseType).typeSymbol.name.decoded)
      //
      val myType = typeOf[Option[String]] //reflect.runtime.universe.Type = acc.typeSignatureIn(caseType)
      val typeParams = myType.asInstanceOf[TypeRefApi].args

      println(typeParams)
      
      if (!typeParams.isEmpty) {
        println("type-parameter: " + typeParams.head.typeSymbol.asType.name.decoded)
      }
      println("--------------------------")
    }
  }

}


//   def log[D <: Entity](domain: D): Unit = macro log_impl[D]
//   def log_impl[D <: Entity: c.WeakTypeTag](c: Context)(domain: c.Expr[D]) = {
//     import c.universe._

//     if (!weakTypeOf[D].typeSymbol.asClass.isCaseClass) c.abort(
//       c.enclosingPosition,
//       "Need something typed as a case class!"
//     ) else c.Expr(
//       Block(
//         accessors[D](c.universe).map(acc =>
//           printfTree(c.universe)(
//             "%s (%s) : %%s\n".format(
//               acc.name.decoded,
//               acc.typeSignature.typeSymbol.name.decoded
//             ),
//             Select(domain.tree.duplicate, acc.name)
//           )
//         ),
//         c.literalUnit.tree
//       )
//     )
//   }
// }