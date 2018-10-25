package examples.geneticProgramming

import lazyOr._
import genetic.RRandom

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
