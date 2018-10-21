package examples.geneticProgramming

import org.scalatest.FunSuite

class SinTest extends FunSuite {
  implicit def strintToVar(s: String): Variable = Variable(s)
  val sin = Sin("x")

  test("Insert variable") {
    val variable = Variable("y")
    val possibleResults: Set[(FunTree, FunTree)] = Set(
      (variable, sin),
      (Sin(variable), "x")
    )
    assert((1 to 100).map(_ => sin.insert(variable)).toSet == possibleResults)
  }

  test("Insert Cos") {
    val cos = Cos("y")
    val possibleResults: Set[(FunTree, FunTree)] = Set(
      (cos, sin),
      (Sin(cos), "x")
    )
    assert((1 to 100).map(_ => sin.insert(cos)).toSet == possibleResults)
  }

  test("Insert Plus") {
    val cos = Cos("y")
    val plus = Plus(cos, "z")
    val possibleResults: Set[(FunTree, FunTree)] = Set(
      (plus, sin),
      (Sin(plus), "x")
    )
    assert((1 to 100).map(_ => sin.insert(plus)).toSet == possibleResults)
  }

  test("Cross variable") {
    val variable = Variable("y")
    val possibleResults: Set[(FunTree, FunTree)] = Set((Variable("y"), Sin("x")), (Sin("y"), Variable("x")))
    assert((1 to 100).map(_ => sin.cross(variable)).toSet == possibleResults)
  }

  test("Cross Cos") {
    val cos = Cos("y")
    val possibleResults: Set[(FunTree, FunTree)] = Set(
      (cos, sin),
      (Sin(cos), "x"),
      (Sin("y"), Cos("x")),
      ("y", Cos(sin))
    )

    assert((1 to 100).map(_ => sin.cross(cos)).toSet == possibleResults)
  }

  test("Cross Plus") {
    val z = Variable("z")
    val y = Variable("y")
    val cos = Cos(y)
    val plus = Plus(cos, z)

    val possibleResults: Set[(FunTree, FunTree)] = Set(
      (plus, sin),
      (cos, Plus(sin, z)),
      (y, Plus(Cos(sin), z)),
      (z, Plus(cos, sin)),
      (Sin(plus), "x"),
      (Sin(cos), Plus("x", z)),
      (Sin(y), Plus(Cos("x"), z)),
      (Sin(z), Plus(cos, "x"))
    )

    assert((1 to 100).map(_ => sin.cross(plus)).toSet == possibleResults)
  }

}
