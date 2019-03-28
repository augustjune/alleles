package alleles.examples.geneticProgramming

import alleles.toolset.RRandom
import lazyOr._

class TreeGen(variables: List[String]) {
  def randomLeaf(): GPTree =
    variables.foldLeft(Value((RRandom.nextDouble() - 0.5) * 100): LazyOr[GPTree])(_ >|| Variable(_))

  def randomNode(): GPTree =
    Sin(randomTree()) >|| Cos(randomTree()) >||
      Plus(randomTree(), randomTree()) >||
      Minus(randomTree(), randomTree()) >||
      Multiply(randomTree(), randomTree()) >||
      Divide(randomTree(), randomTree())

  def randomTree(): GPTree =
    randomLeaf() >|| randomNode()
}
