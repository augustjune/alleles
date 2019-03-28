package genetic.engines.parallel

import genetic.engines.Progress
import genetic.genotype.Fitness.Rated
import genetic.genotype.{Join, Variation}
import genetic.{Epoch, Population}

import scala.collection.parallel.immutable.ParVector

object ParallelProgress extends Progress {
  def nextGeneration[A: Join : Variation](ratedPop: Population[Rated[A]],
                                          operators: Epoch): Population[A] =
    ParVector.fill(ratedPop.size / 2)(())
      .map(_ => operators.selection.single(ratedPop))
      .flatMap(operators.crossover.single(_))
      .map(operators.mutation.single(_))
      .seq
}
