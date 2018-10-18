package examples.geneticProgramming

import genetic.RRandom
import genetic.collections.IterablePair
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

  def chance(): Boolean = RRandom.nextBoolean()

  // Starting JOIN Section
  sealed trait Incomplete
  object IncompleteNil extends Incomplete
  case class IncompleteOne[T <: FunTree](arg: Incomplete, ap: FunTree => T) extends Incomplete
  case class LeftIncomplete[T <: FunTree](left: Incomplete, right: FunTree, ap: (FunTree, FunTree) => T) extends Incomplete
  case class RightIncomplete[T <: FunTree](left: FunTree, right: Incomplete, ap: (FunTree, FunTree) => T) extends Incomplete

  def complete(incomplete: Incomplete, addOn: FunTree): FunTree = incomplete match {
    case IncompleteNil => addOn
    case IncompleteOne(arg, ap) => ap(complete(arg, addOn))
    case LeftIncomplete(left, right, ap) => ap(complete(left, addOn), right)
    case RightIncomplete(left, right, ap) => ap(left, complete(right, addOn))
  }

  def split(t: FunTree): (Incomplete, FunTree) = t match {
    case X | Y | Value(_) => (IncompleteNil, t)
    case Sin(a) => splitOne(a, Sin)
    case Cos(a) => splitOne(a, Cos)
    case Plus(a, b) => splitTwo(a, b, Plus)
    case Minus(a, b) => splitTwo(a, b, Minus)
    case Multiply(a, b) => splitTwo(a, b, Multiply)
    case Divide(a, b) => splitTwo(a, b, Divide)
  }

  def splitOne[T <: FunTree](arg: FunTree, ap: FunTree => T): (Incomplete, FunTree) = {
    val (inc, full) = split(arg)
    (IncompleteOne(inc, ap), full)
  }

  def splitTwo[T <: FunTree](a: FunTree, b: FunTree, ap: (FunTree, FunTree) => T): (Incomplete, FunTree) =
    if (chance()) {
      val (inc, full) = split(a)
      (LeftIncomplete(inc, b, ap), full)
    } else {
      val (inc, full) = split(b)
      (RightIncomplete(a, inc, ap), full)
    }

  val join: Join[FunTree] = (x: FunTree, y: FunTree) => {
    val (incomplete1, full1) = split(x)
    val (incomplete2, full2) = split(y)
    IterablePair(complete(incomplete1, full2), complete(incomplete2, full1))
  }

  // Starting Modification Section
  val modification: Modification[FunTree] = ???
}
