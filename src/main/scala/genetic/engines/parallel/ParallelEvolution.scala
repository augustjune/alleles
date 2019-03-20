package genetic.engines.parallel

import genetic.engines.Evolution
import genetic.genotype.Fitness.Rated
import genetic.genotype.{Join, Variation}
import genetic.{OperatorSet, Population}

import scala.collection.parallel.immutable.ParVector

object ParallelEvolution extends Evolution {
  def nextGeneration[A: Join : Variation](ratedPop: Population[Rated[A]],
                                          operators: OperatorSet): Population[A] =
    ParVector.fill(ratedPop.size / 2)(())
      .map(_ => operators.selection.single(ratedPop))
      .flatMap(operators.crossover.single(_))
      .map(operators.mutation.single(_))
      .seq
}
