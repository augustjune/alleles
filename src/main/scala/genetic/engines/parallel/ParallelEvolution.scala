package genetic.engines.parallel

import genetic.engines.{Evolution, Rated}
import genetic.genotype.{Join, Variation}
import genetic.{OperatorSet, Population}

import scala.collection.parallel.immutable.ParVector

object ParallelEvolution extends Evolution {
  def nextGeneration[A: Join : Variation](ratedPop: Population[Rated[A]],
                                          operators: OperatorSet): Population[A] = operators match {
    case OperatorSet(selection, crossover, mutation) =>
      ParVector.fill(ratedPop.size / 2)(())
        .map(_ => selection.single(ratedPop))
        .flatMap(crossover.single(_))
        .map(mutation.single(_))
        .seq
  }
}
