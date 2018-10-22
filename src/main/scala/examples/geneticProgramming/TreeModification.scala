package examples.geneticProgramming

import genetic.RRandom
import genetic.genotype.Modification
import lazyOr._

object TreeModification extends Modification[FunTree] {
  private def randomLeaf(): FunTree = Variable("x") >|| Variable("y") >|| Value((RRandom.nextDouble() - 0.5) * 100)

  private def randomNode(): FunTree =
    Sin(randomTree()) >|| Cos(randomTree()) >||
      Plus(randomTree(), randomTree()) >||
      Minus(randomTree(), randomTree()) >||
      Multiply(randomTree(), randomTree()) >||
      Divide(randomTree(), randomTree())

  private def randomTree(): FunTree =
    randomLeaf() >|| randomNode()

  private def modify1(ap: FunTree => FunTree, arg: FunTree): FunTree =
    randomTree() >|| ap(modify(arg))

  private def modify2(ap: (FunTree, FunTree) => FunTree, a: FunTree, b: FunTree): FunTree =
    randomTree() >|| ap(modify(a), b) >|| ap(a, modify(b))

  def modify(t: FunTree): FunTree = t.insert(randomTree())._1
}
