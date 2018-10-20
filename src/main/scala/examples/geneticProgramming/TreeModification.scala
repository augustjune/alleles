package examples.geneticProgramming

import examples.geneticProgramming.Tree._
import genetic.RRandom
import genetic.genotype.Modification

object TreeModification extends Modification[FunTree] {
  private def randomLeaf(): FunTree = RRandom.nextInt(3) match {
    case 0 => X
    case 1 => Y
    case 2 => Value(RRandom.nextDouble() * 100)
  }

  private def randomNode(): FunTree = RRandom.nextInt(6) match {
    case 0 => Sin(randomTree())
    case 1 => Cos(randomTree())
    case 2 => Plus(randomTree(), randomTree())
    case 3 => Minus(randomTree(), randomTree())
    case 4 => Multiply(randomTree(), randomTree())
    case 5 => Divide(randomTree(), randomTree())
  }

  private def randomTree(): FunTree =
    if (RRandom.shot(0.5)) randomLeaf()
    else randomNode()

  private def modify1(ap: FunTree => FunTree, arg: FunTree): FunTree =
    if (RRandom.shot(0.5)) randomTree() else ap(modify(arg))

  private def modify2(ap: (FunTree, FunTree) => FunTree, a: FunTree, b: FunTree): FunTree = RRandom.nextInt(3) match {
    case 0 => randomTree()
    case 1 => ap(modify(a), b)
    case 2 => ap(a, modify(b))
  }

  def modify(t: FunTree): FunTree = t match {
    case X | Y | Value(_) => randomTree()
    case Sin(a) => modify1(Sin, a)
    case Cos(a) => modify1(Sin, a)
    case Plus(a, b) => modify2(Plus, a, b)
    case Minus(a, b) => modify2(Minus, a, b)
    case Multiply(a, b) => modify2(Multiply, a, b)
    case Divide(a, b) => modify2(Divide, a, b)
  }
}
