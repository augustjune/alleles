package examples.geneticProgramming

import org.scalatest.FunSuite

class VariableTest extends FunSuite {
  val variable: GPTree = Variable("x")

  test("Insert") {
    val var1 = Variable("y")
    assert(variable.insert(var1) == (var1, variable))

    val sin = Sin(var1)
    assert(variable.insert(sin) == (sin, variable))

    val plus = Plus(var1, sin)
    assert(variable.insert(plus) == (plus, variable))
  }

  test("Cross variable") {
    val var1 = Variable("y")
    val possibleResults: Set[(GPTree, GPTree)] = Set((var1, variable))
    assert((1 to 100).map(_ => variable.cross(var1)).toSet == possibleResults)
  }

  test("Cross Sin") {
    val var1 = Variable("y")
    val sin = Sin(var1)
    val possibleResults: Set[(GPTree, GPTree)] = Set((sin, variable), (var1, Sin(variable)))
    assert((1 to 100).map(_ => variable.cross(sin)).toSet == possibleResults)
  }

  test("Cross Plus") {
    val var1 = Variable("y")
    val sin = Sin(var1)
    val plus = Plus(var1, sin)
    val possibleResults: Set[(GPTree, GPTree)] = Set(
      (plus, variable),
      (var1, Plus(variable, Sin(var1))),
      (sin, Plus(var1, variable)),
      (var1, Plus(var1, Sin(variable)))
    )
    assert((1 to 100).map(_ => variable.cross(plus)).toSet == possibleResults)
  }

}
