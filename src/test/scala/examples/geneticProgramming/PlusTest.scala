package examples.geneticProgramming

import org.scalatest.FunSuite

class PlusTest extends FunSuite {
  val x = Variable("x")
  val y = Variable("y")
  val sin = Sin(x)
  val plus = Plus(sin, y)

  test("Insert Variable") {
    val variable = Variable("z")
    val possibleResults: Set[(FunTree, FunTree)] = Set(
      (variable, plus),
      (Plus(variable, y), sin),
      (Plus(Sin(variable), y), x),
      (Plus(sin, variable), y)
    )
    assert((1 to 100).map(_ => plus.insert(variable)).toSet == possibleResults)
  }

  test("Insert Cos") {
    val variable = Variable("z")
    val cos = Cos(variable)
    val possibleResults: Set[(FunTree, FunTree)] = Set(
      (cos, plus),
      (Plus(cos, y), sin),
      (Plus(Sin(cos), y), x),
      (Plus(sin, cos), y)
    )
    assert((1 to 100).map(_ => plus.insert(cos)).toSet == possibleResults)
  }

  test("Insert Minus") {
    val minus = Minus(Variable("z"), Variable("zz"))

    val possibleResults: Set[(FunTree, FunTree)] = Set(
      (minus, plus),
      (Plus(minus, y), sin),
      (Plus(Sin(minus), y), x),
      (Plus(sin, minus), y)
    )
    assert((1 to 100).map(_ => plus.insert(minus)).toSet == possibleResults)
  }

  test("Cross Cos") {
    val variable = Variable("z")
    val cos = Cos(variable)
    val possibleResults: Set[(FunTree, FunTree)] = Set(
      (cos, plus),
      (Plus(cos, y), sin),
      (Plus(Sin(cos), y), x),
      (Plus(sin, cos), y),
      (variable, Cos(plus)),
      (Plus(variable, y), Cos(sin)),
      (Plus(Sin(variable), y), Cos(x)),
      (Plus(sin, variable), Cos(y))
    )
    assert((1 to 100).map(_ => plus.cross(cos)).toSet == possibleResults)
  }

  test("Cross Minus") {
    val var1 = Variable("z")
    val var2 = Variable("zz")
    val minus = Minus(var1, var2)

    val possibleResults: Set[(FunTree, FunTree)] = Set(
      (minus, plus),
      (Plus(minus, y), sin),
      (Plus(Sin(minus), y), x),
      (Plus(sin, minus), y),
      (var1, Minus(plus, var2)),
      (Plus(var1, y), Minus(sin, var2)),
      (Plus(Sin(var1), y), Minus(x, var2)),
      (Plus(sin, var1), Minus(y, var2)),
      (var2, Minus(var1, plus)),
      (Plus(var2, y), Minus(var1, sin)),
      (Plus(Sin(var2), y), Minus(var1, x)),
      (Plus(sin, var2), Minus(var1, y))
    )
    assert((1 to 100).map(_ => plus.cross(minus)).toSet == possibleResults)
  }

}
