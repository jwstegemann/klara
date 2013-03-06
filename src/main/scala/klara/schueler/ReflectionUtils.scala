package klara.schueler

trait ReflectionUtils {
  import scala.reflect.api.Universe

  def accessors[A: u.WeakTypeTag](u: Universe) = {
    import u._

    u.weakTypeOf[A].declarations.collect {
      case acc: MethodSymbol if acc.isCaseAccessor => acc
    }.toList
  }

  def printfTree(u: Universe)(format: String, trees: u.Tree*) = {
    import u._

    Apply(
      Select(reify(Predef).tree, "printf"),
      Literal(Constant(format)) :: trees.toList
    )
  }
}