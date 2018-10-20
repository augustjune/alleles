package examples.geneticProgramming

import genetic.genotype.{Join, Modification}

object Tree extends App {

  sealed trait FunTree

  final case object X extends FunTree

  final case object Y extends FunTree

  final case class Value(v: Double) extends FunTree

  final case class Sin(a: FunTree) extends FunTree

  final case class Cos(a: FunTree) extends FunTree

  final case class Plus(a: FunTree, b: FunTree) extends FunTree

  final case class Minus(a: FunTree, b: FunTree) extends FunTree

  final case class Multiply(a: FunTree, b: FunTree) extends FunTree

  final case class Divide(a: FunTree, b: FunTree) extends FunTree

  trait Calc[T] {
    def eval(t: T): Double
  }

  def calc(x: Double, y: Double): Calc[FunTree] = new Calc[FunTree] {
    def eval(t: FunTree): Double = t match {
      case X => x
      case Y => y
      case Value(v) => v
      case Plus(a, b) => eval(a) + eval(b)
      case Minus(a, b) => eval(a) - eval(b)
      case Multiply(a, b) => eval(a) * eval(b)
      case Divide(a, b) => eval(a) / eval(b)
      case Sin(a) => math.sin(eval(a))
      case Cos(a) => math.cos(eval(a))
    }
  }

  implicit val join: Join[FunTree] = TreeJoin
  implicit val modification: Modification[FunTree] = TreeModification

}
