package examples.geneticProgramming

import genetic.collections.IterablePair
import genetic.genotype.Join

object TreeJoin extends Join[FunTree] {

  def cross(x: FunTree, y: FunTree): IterablePair[FunTree] = x.cross(y) match {
    case (xs, ys) => IterablePair(xs, ys)
  }
}
